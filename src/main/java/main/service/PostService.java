package main.service;

import main.api.request.LikeDislikeRequest;
import main.api.request.NewPostRequest;
import main.api.request.PostModerationRequest;
import main.api.response.CalendarResponse;
import main.api.response.FailResponse;
import main.api.response.PostResponse;
import main.api.response.PostResponseId;
import main.model.*;
import main.repository.*;
import main.model.DTO.CommentForPostTDO;
import main.model.DTO.PostTDO;
import main.model.DTO.UserForCommentForPostTDO;
import main.model.DTO.UserForPostTDO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostService {

    private final Logger logger = LogManager.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagsRepository tagsRepository;
    private final Tag2PostRepository tag2PostRepository;
    private final PostVotesRepository postVotesRepository;
    private final GlobalSettingsRepository globalSettingsRepository;

    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");

    private final int LIKE_VALUE = 1;
    private final int DISLIKE_VALUE = 0;

    @Autowired
    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       TagsRepository tagsRepository,
                       Tag2PostRepository tag2PostRepository,
                       PostVotesRepository postVotesRepository,
                       GlobalSettingsRepository globalSettingsRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagsRepository = tagsRepository;
        this.tag2PostRepository = tag2PostRepository;
        this.postVotesRepository = postVotesRepository;
        this.globalSettingsRepository = globalSettingsRepository;
    }

    public PostResponse getAllPostResponse(int limit,
                                           int offset,
                                           String mode) {
        if (mode == null) {
            logger.info("Параметр mode не задан - возвращаем последние посты");
            mode = "recent";
        }
        if (mode.equalsIgnoreCase("recent")) {
            logger.info("Возвращаем последние посты");
            return createResponse(postRepository.findPostsByTimeNewFist(PageRequest.of(offset / limit, limit)));
        } else if (mode.equalsIgnoreCase("early")) {
            logger.info("Возвращаем старые посты");
            return createResponse(postRepository.findPostsByTimeOldFist(PageRequest.of(offset / limit, limit)));
        } else if (mode.equalsIgnoreCase("popular")) {
            logger.info("Возвращаем самые обсуждаемые посты");
            return createResponse(postRepository.findPostsMostPopular(PageRequest.of(offset / limit, limit)));
        } else if (mode.equalsIgnoreCase("best")) {
            logger.info("Возвращаем лучшие по лайкам посты");
            return createResponse(postRepository.findPostsBest(PageRequest.of(offset / limit, limit)));
        }
        return null;
    }

    public PostResponse getSearchPostResponse(int limit, int offset, String query) {
        logger.info("Поиск постов по строке " + query);
        if (query.trim().length() == 0) {
            logger.info("Строка для поиска не задана - возвращаем последние по дате посты");
            return getAllPostResponse(limit, offset, "recent");
        } else {
            logger.info("Возвращаем найденные по запросу " + query + " посты");
            return createResponse(postRepository.findTextInPost(PageRequest.of(offset / limit, limit), query));
        }
    }

    public CalendarResponse getCalendar() {
        CalendarResponse calendar = new CalendarResponse();
        List<Integer> years = new ArrayList<>();
        HashMap<String, Integer> posts = new HashMap<>();
        logger.info("Получение всех постов из базы");
        List<Post> allCalendarPosts = postRepository.getCalendar();
        logger.info("Формирование постов для ответа");
        for (Post post : allCalendarPosts) {
            Date date = post.getTime();
            int year = Integer.parseInt(formatYear.format(date));
            String monthDay = formatDate.format(date);
            if (!years.contains(year)) {
                years.add(year);
            }
            if (!posts.containsKey(monthDay)) {
                posts.put(monthDay, 1);
            } else {
                posts.put(monthDay, posts.get(monthDay) + 1);
            }
        }
        calendar.setPosts(posts);
        calendar.setYears(years);
        logger.info("Ответ сформирован");
        return calendar;
    }

    public PostResponse getSearchByDatePostResponse(int limit, int offset, String date) {
        logger.info("Поиск постов по дате " + date);
        return createResponse(postRepository.getSearchByDatePostResponse(PageRequest.of(offset / limit, limit), date));
    }

    public PostResponse getSearchByTagPostResponse(int limit, int offset, String tag) {
        logger.info("Поиск постов по тэгу " + tag);
        return createResponse(postRepository.getSearchByTagPostResponse(PageRequest.of(offset / limit, limit), tag));
    }

    public PostResponseId getPostById(int id) {
        logger.info("Поиск поста по id " + id);
        PostResponseId postResponseId = new PostResponseId();
        logger.info("Проверка авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Post post;
        if (findEmail == null) {
            logger.info("Пользователь не авторизован - ищем пост по id");
            post = postRepository.getPostById(id);
        } else {
            logger.info("Пользователь авторизован - ищем пост по id даже (неактивные посты отобразить)");
            post = postRepository.getPostByIdAuth(id);
        }
        if (post == null) {
            logger.error("Ошибка получения поста - пост с id " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User userInDB = userRepository.findByEmail(findEmail);
        logger.info("Поиск пользователя по почте " + findEmail);
        if (userInDB != null) {
            if (userInDB.isIs_moderator() == 0 && userInDB != post.getUser()) {
                logger.info("Пользователь не модератор, происходит просмотр не своего поста");
                post.setViewCount(post.getViewCount() + 1);
                postRepository.save(post);
            }
        } else {
            logger.info("Пользователь не зарегистрирован, происходит просмотр поста");
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        }
        logger.info("Формирование ответа для вывода поста");
        postResponseId.setId(post.getId());
        postResponseId.setTimestamp(post.getTime().getTime() / 1000);
        postResponseId.setActive(post.isIs_active() == 1);
        UserForPostTDO user = new UserForPostTDO();
        user.setId(post.getUser().getId());
        user.setName(post.getUser().getName());
        postResponseId.setUser(user);
        postResponseId.setTitle(post.getTitle());
        postResponseId.setText(post.getText());
        postResponseId.setLikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 1).count());
        postResponseId.setDislikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 0).count());
        postResponseId.setViewCount(post.getViewCount());
        List<CommentForPostTDO> comments = new ArrayList<>();
        Set<PostComments> postComments = post.getPostComments();
        for (PostComments com : postComments) {
            CommentForPostTDO comment = new CommentForPostTDO();
            comment.setId(com.getId());
            comment.setTimestamp(com.getTime().getTime() / 1000);
            comment.setText(com.getText());
            UserForCommentForPostTDO userTestForPostTest = new UserForCommentForPostTDO();
            userTestForPostTest.setId(com.getUser().getId());
            userTestForPostTest.setName(com.getUser().getName());
            userTestForPostTest.setPhoto(com.getUser().getPhoto());
            comment.setUser(userTestForPostTest);
            comments.add(comment);
        }
        comments.sort(Comparator.comparing(CommentForPostTDO::getTimestamp).reversed());
        postResponseId.setComments(comments);
        Set<Tags> tagSet = post.getTags();
        List<String> tags = new ArrayList<>();
        for (Tags tag : tagSet) {
            tags.add(tag.getName());
        }
        postResponseId.setTags(tags);
        logger.info("Ответ по посту " + id + " сформирован для вывода");
        return postResponseId;
    }

    public PostTDO newPostTDO(Post post) {
        logger.info("Формирование поста " + post.getId());
        PostTDO postTDO = new PostTDO();
        postTDO.setId(post.getId());
        postTDO.setTimestamp(post.getTime().getTime() / 1000);
        UserForPostTDO userForPostTDO = new UserForPostTDO();
        userForPostTDO.setId(post.getUser().getId());
        userForPostTDO.setName(post.getUser().getName());
        postTDO.setUser(userForPostTDO);
        postTDO.setTitle(post.getTitle());
        String tempText = Jsoup.parse(post.getText()).text();
        if (tempText.length() > 150) {
            String announce = tempText.substring(0, 150);
            postTDO.setAnnounce(announce.substring(0, announce.lastIndexOf(" ")) + "...");
        } else {
            postTDO.setAnnounce(tempText);
        }
        postTDO.setLikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 1).count());
        postTDO.setDislikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 0).count());
        postTDO.setCommentCount(post.getPostComments().size());
        postTDO.setViewCount(post.getViewCount());
        logger.info("Пост " + post.getId() + " сформирован");
        return postTDO;
    }

    public PostResponse getPostModeration(int limit,
                                          int offset,
                                          String status) {
        logger.info("Начало проверка авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Авторизация прошла успешно");
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            logger.error("Ошибка! Пользователь с почтовым ящиком " + findEmail + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (userInDB.isIs_moderator() == 0) {
            logger.error("Ошибка! Пользователь не является модератором");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        int modId = userInDB.getId();
        if (status.equalsIgnoreCase("NEW")) {
            logger.info("Вывод постов со статусом NEW");
            return createResponse(postRepository.getNewPostsResponse(PageRequest.of(offset / limit, limit)));
        }
        if (status.equalsIgnoreCase("DECLINED")) {
            logger.info("Вывод постов со статусом DECLINED");
            return createResponse(postRepository.getDeclinedPostsResponse(PageRequest.of(offset / limit, limit), modId));
        }
        if (status.equalsIgnoreCase("ACCEPTED")) {
            logger.info("Вывод постов со статусом ACCEPTED");
            return createResponse(postRepository.getAcceptedPostsResponse(PageRequest.of(offset / limit, limit), modId));
        }
        return null;
    }

    private PostResponse createResponse(Page<Post> postsToShow) {
        logger.info("Формирование списка постов для вывода");
        PostResponse response = new PostResponse();
        List<PostTDO> posts = new ArrayList<>();
        int postCount = (int) postsToShow.getTotalElements();
        for (Post post : postsToShow) {
            PostTDO postTDO = newPostTDO(post);
            if (!posts.contains(postTDO)) {
                posts.add(postTDO);
            }
        }
        response.setCount(postCount);
        response.setPosts(posts);
        logger.info("Вывод постов для показа");
        return response;
    }

    public PostResponse getMyPostsResponse(int limit,
                                           int offset,
                                           String status) {
        logger.info("Начало авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Авторизация прошла успешно");
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            logger.error("Ошибка! Пользователь с почтовым ящиком " + findEmail + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        int userId = userInDB.getId();
        if (status == null) {
            logger.error("Ошибка! Статус постов для вывода не задан");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (status.equalsIgnoreCase("inactive")) {
            logger.info("Вывод на экран неактивных постов пользователя " + userInDB.getName());
            return createResponse(postRepository.findMyInActivePosts(PageRequest.of(offset / limit, limit), userId));
        }
        if (status.equalsIgnoreCase("pending")) {
            logger.info("Вывод на экран еще не утвержденных постов пользователя " + userInDB.getName());
            return createResponse(postRepository.findMyPendingPosts(PageRequest.of(offset / limit, limit), userId));
        }
        if (status.equalsIgnoreCase("declined")) {
            logger.info("Вывод на экран отклоненных постов пользователя " + userInDB.getName());
            return createResponse(postRepository.findMyDeclinedPosts(PageRequest.of(offset / limit, limit), userId));
        }
        if (status.equalsIgnoreCase("published")) {
            logger.info("Вывод на экран опубликованных постов пользователя " + userInDB.getName());
            return createResponse(postRepository.findMyAcceptedPosts(PageRequest.of(offset / limit, limit), userId));
        }
        return null;
    }

    public static void checkAuthorized(String email) {
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public FailResponse postNewPost(NewPostRequest request,
                                    BindingResult error) {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during logging = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        logger.info("Начало авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Авторизация прошла успешно");
        User userInDB = userRepository.findByEmail(findEmail);
        boolean moderationMode = globalSettingsRepository.findPremoderationMode().equals("YES");
        if (userInDB == null) {
            logger.error("Ошибка! Пользователь с почтовым ящиком " + findEmail + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (request.getTitle().length() < 3) {
            logger.error("Ошибка создания нового поста - Заголовок не установлен");
            errors.put("title", "Заголовок не установлен");
        }
        if (request.getText().length() < 50) {
            logger.error("Ошибка создания нового поста - Текст публикации слишком короткий");
            errors.put("text", "Текст публикации слишком короткий");
        }
        if (!errors.isEmpty()) {
            logger.error("Новый пост не создан из-за возникших ошибок");
            response.setResult(false);
            response.setErrors(errors);
            return response;
        } else {
            logger.info("Начало создания ноого поста");
            Post post = new Post();
            post.setIs_active(request.getActive());
            if (!moderationMode && request.getActive() == 1) {
                post.setModeration_status(ModerationStatus.ACCEPTED);
            } else {
                post.setModeration_status(ModerationStatus.NEW);
            }
            post.setUser(userInDB);
            if ((request.getTimestamp().getTime() * 1000) < (new Timestamp(System.currentTimeMillis())).getTime()) {
                post.setTime(new Timestamp(System.currentTimeMillis()));
            } else {
                post.setTime(new Timestamp(request.getTimestamp().getTime() * 1000));
            }
            post.setTitle(request.getTitle());
            post.setText(request.getText());
            post.setViewCount(0);
            postRepository.save(post);
            logger.info("Новый пост с названием " + post.getTitle() + " сформирован и сохранен в базу");

            Set<String> tagsFromRequest = request.getTags();
            checkTags(tagsFromRequest);
            checkTags2Post(tagsFromRequest, post);
        }
        response.setResult(true);
        return response;
    }

    private void createNewTag2Post(Tags tag, Post post) {
        logger.info("Создание связки пост №" + post.getId() + " - тэг №" + tag.getId());
        Tag2Post tag2Post = new Tag2Post();
        tag2Post.setTag(tag);
        tag2Post.setPost(post);
        tag2PostRepository.save(tag2Post);
    }

    public FailResponse putPost(int id,
                                NewPostRequest request,
                                BindingResult error) {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during logging = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        logger.info("Начало авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Авторизация прошла успешно");
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            logger.error("Ошибка! Пользователь с почтовым ящиком " + findEmail + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (request.getTitle().length() < 3) {
            logger.error("Ошибка редактирования поста - Текст публикации слишком короткий");
            errors.put("title", "Заголовок не установлен");
        }
        if (request.getText().length() < 50) {
            logger.error("Ошибка редактирования поста - Текст публикации слишком короткий");
            errors.put("text", "Текст публикации слишком короткий");
        }
        if (!errors.isEmpty()) {
            logger.info("Попытка редактирования поста неуспешна");
            response.setResult(false);
            response.setErrors(errors);
        } else {
            Post post = postRepository.getPostByIdAuth(id);
            if ((request.getTimestamp().getTime() * 1000) < (new Timestamp(System.currentTimeMillis())).getTime()) {
                post.setTime(new Timestamp(System.currentTimeMillis()));
            } else {
                post.setTime(new Timestamp(request.getTimestamp().getTime() * 1000));
            }
            post.setIs_active(request.getActive());
            post.setTitle(request.getTitle());
            post.setText(request.getText());
            logger.info("Попытка редактирования поста успешна");
            postRepository.save(post);
            Set<String> tagsFromRequest = request.getTags();
            checkTags(tagsFromRequest);
            for (String tag : tagsFromRequest) {
                Tags tagFromDB = tagsRepository.findTagByName(tag);
                Tag2Post tag2Post = tag2PostRepository.findByPostAndTag(post.getId(), tagFromDB.getId());
                if (tag2Post == null) {
                    createNewTag2Post(tagFromDB, post);
                }
            }
            response.setResult(true);
        }
        return response;
    }

    public FailResponse postModeration(PostModerationRequest request,
                                       BindingResult error) {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during logging = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info("Начало авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Авторизация прошла успешно");
        FailResponse response = new FailResponse();
        User moderator = findModeratorByEmail(findEmail);
        if (moderator == null) {
            logger.error("Ошибка - Пользователь не является модератором");
            response.setResult(false);
        } else {
            if (request == null) {
                logger.error("Ошибка - с фронта пришел пустой запрос");
                response.setResult(false);
                return response;
            }
            Post postToModerate = postRepository.getPostByIdModerate(request.getPostId());
            postToModerate.setModeration_status(request.getDecision().equals("accept")
                    ? ModerationStatus.ACCEPTED : ModerationStatus.DECLINED);
            postToModerate.setModerator_id(moderator.getId());
            logger.info("Пост №" + request.getPostId() + " прошел модерацию и получил статус " + postToModerate.getModeration_status());
            postRepository.save(postToModerate);
            response.setResult(true);
        }
        return response;
    }

    private User findModeratorByEmail(String email) {
        return userRepository.findModeratorByEmail(email);
    }

    public FailResponse postLike(LikeDislikeRequest request,
                                 BindingResult error) {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during logging = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info("Начало авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Авторизация прошла успешно");
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            logger.error("Ошибка! Пользователь с почтовым ящиком " + findEmail + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Post postInDB = postRepository.getPostById(request.getPostId());
        if (postInDB == null) {
            logger.error("Ошибка - пост не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        FailResponse response = new FailResponse();
        PostVotes postVotesInDB = postVotesRepository.findByUserAndPost(userInDB.getId(), request.getPostId());
        if (postVotesInDB == null) {
            response.setResult(newPostVotesLikeOrDislike(userInDB, postInDB, LIKE_VALUE));
            logger.info("Посту успешно прожат лайк");
        } else {
            if (postVotesInDB.getValue() == LIKE_VALUE) {
                logger.error("Вы уже поставили лайк этому посту");
                response.setResult(false);
            } else {
                postVotesInDB.setValue(LIKE_VALUE);
                logger.info("Посту успешно прожат лайк вместо дизлайка");
                response.setResult(true);
            }
        }
        return response;
    }

    public FailResponse postDislike(LikeDislikeRequest request,
                                    BindingResult error) {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during logging = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info("Начало авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Авторизация прошла успешно");
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            logger.error("Ошибка! Пользователь с почтовым ящиком " + findEmail + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Post postInDB = postRepository.getPostById(request.getPostId());
        if (postInDB == null) {
            logger.error("Ошибка - пост не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        FailResponse response = new FailResponse();
        PostVotes postVotesInDB = postVotesRepository.findByUserAndPost(userInDB.getId(), request.getPostId());
        if (postVotesInDB == null) {
            response.setResult(newPostVotesLikeOrDislike(userInDB, postInDB, DISLIKE_VALUE));
            logger.info("Посту успешно прожат дизлайк");
        } else {
            if (postVotesInDB.getValue() == DISLIKE_VALUE) {
                logger.error("Вы уже поставили дизлайк этому посту");
                response.setResult(false);
            } else {
                postVotesInDB.setValue(DISLIKE_VALUE);
                logger.info("Посту успешно прожат дизлайк вместо лайка");
                response.setResult(true);
            }
        }
        return response;
    }

    private boolean newPostVotesLikeOrDislike(User user, Post post, int value) {
        logger.info("Создание нового postVote в БД");
        PostVotes newPostVote = new PostVotes();
        newPostVote.setUser(user);
        newPostVote.setPostId(post);
        newPostVote.setTime(new Timestamp(System.currentTimeMillis()));
        newPostVote.setValue(value);
        postVotesRepository.save(newPostVote);
        return true;
    }

    private void checkTags (Set<String> tags) {
        logger.info("Проверка списка тэгов для поста (новые тэги пишем базу)");
        for (String tag : tags) {
            Tags fromDB = tagsRepository.findTagByName(tag);
            if (fromDB == null) {
                logger.info("Тэг " + tag + " отсутсвует в базе, записываем в БД");
                Tags newTag = new Tags();
                newTag.setName(tag);
                tagsRepository.save(newTag);
            }
        }
    }

    private void checkTags2Post (Set<String> tags, Post post) {
        for (String tag : tags) {
            logger.info("Создание новой связки поста №" + post.getId() + " и тэга " + tag);
            Tags tagFromDB = tagsRepository.findTagByName(tag);
            createNewTag2Post(tagFromDB, post);
        }
    }
}
