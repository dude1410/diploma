package main.service;

import main.api.request.NewPostRequest;
import main.api.request.PostModerationRequest;
import main.api.response.CalendarResponse;
import main.api.response.FailResponse;
import main.api.response.PostResponse;
import main.api.response.PostResponseId;
import main.model.*;
import main.repository.PostRepository;
import main.model.DTO.CommentTestForPost;
import main.model.DTO.PostTest;
import main.model.DTO.UserTestForCommentForPost;
import main.model.DTO.UserTestForPostTest;
import main.repository.Tag2PostRepository;
import main.repository.TagsRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagsRepository tagsRepository;
    private final Tag2PostRepository tag2PostRepository;

    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");

    @Autowired
    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       TagsRepository tagsRepository,
                       Tag2PostRepository tag2PostRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagsRepository = tagsRepository;
        this.tag2PostRepository = tag2PostRepository;
    }

    public PostResponse getAllPostResponse(int limit, int offset, String mode) {

        if (mode == null) {
            mode = "recent";
        }
        if (mode.equalsIgnoreCase("recent")) {
            return createResponse(postRepository.findPostsByTimeNewFist(PageRequest.of(offset / limit, limit)));
        } else if (mode.equalsIgnoreCase("early")) {
            return createResponse(postRepository.findPostsByTimeOldFist(PageRequest.of(offset / limit, limit)));
        } else if (mode.equalsIgnoreCase("popular")) {
            return createResponse(postRepository.findPostsMostPopular(PageRequest.of(offset / limit, limit)));
        } else if (mode.equalsIgnoreCase("best")) {
            return createResponse(postRepository.findPostsBest(PageRequest.of(offset / limit, limit)));
        }
        return null;
    }

    public PostResponse getSearchPostResponse(int limit, int offset, String query) {

        if (query.trim().length() == 0) {
            return getAllPostResponse(limit, offset, "recent");
        } else {
            return createResponse(postRepository.findTextInPost(PageRequest.of(offset / limit, limit), query));
        }
    }

    public CalendarResponse getCalendar() {
        CalendarResponse calendar = new CalendarResponse();
        List<Integer> years = new ArrayList<>();
        HashMap<String, Integer> posts = new HashMap<>();

        List<Post> allCalendarPosts = postRepository.getCalendar();

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
        return calendar;
    }

    public PostResponse getSearchByDatePostResponse(int limit, int offset, String date) {
        return createResponse(postRepository.getSearchByDatePostResponse(PageRequest.of(offset / limit, limit), date));
    }

    public PostResponse getSearchByTagPostResponse(int limit, int offset, String tag) {
        return createResponse(postRepository.getSearchByTagPostResponse(PageRequest.of(offset / limit, limit), tag));
    }

    // todo: добавить код после авторизации
    // todo: изменить тип возврата для active
    public PostResponseId getPostById(int id) {
        PostResponseId postResponseId = new PostResponseId();

        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Post post;
        if (findEmail == null) {
            post = postRepository.getPostById(id);
        } else {
            post = postRepository.getPostByIdAuth(id);
        }
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        postResponseId.setId(post.getId());
        postResponseId.setTimestamp(post.getTime().getTime() / 1000);
        if (post.isIs_active() == 1) {
            postResponseId.setActive(true);
        } else {
            postResponseId.setActive(false);
        }
        UserTestForPostTest user = new UserTestForPostTest();
        user.setId(post.getUser().getId());
        user.setName(post.getUser().getName());
        postResponseId.setUser(user);
        postResponseId.setTitle(post.getTitle());
        postResponseId.setText(post.getText());
        postResponseId.setLikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 1).count());
        postResponseId.setDislikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 0).count());
        postResponseId.setViewCount(post.getViewCount());
        List<CommentTestForPost> comments = new ArrayList<>();
        Set<PostComments> postComments = post.getPostComments();
        for (PostComments com : postComments) {
            CommentTestForPost comment = new CommentTestForPost();
            comment.setId(com.getId());
            comment.setTimestamp(com.getTime().getTime() / 1000);
            comment.setText(com.getText());
            UserTestForCommentForPost userTestForPostTest = new UserTestForCommentForPost();
            userTestForPostTest.setId(com.getUser().getId());
            userTestForPostTest.setName(com.getUser().getName());
            userTestForPostTest.setPhoto(com.getUser().getPhoto());
            comment.setUser(userTestForPostTest);
            comments.add(comment);
        }
        comments.sort(Comparator.comparing(CommentTestForPost::getTimestamp).reversed());
        postResponseId.setComments(comments);
        Set<Tags> tagSet = post.getTags();
        List<String> tags = new ArrayList<>();
        for (Tags tag : tagSet) {
            tags.add(tag.getName());
        }
        postResponseId.setTags(tags);
        return postResponseId;
    }

    public PostTest newPostTest(Post post) {
        PostTest postTest = new PostTest();
        postTest.setId(post.getId());
        postTest.setTimestamp(post.getTime().getTime() / 1000);
        UserTestForPostTest userTestForPostTest = new UserTestForPostTest();
        userTestForPostTest.setId(post.getUser().getId());
        userTestForPostTest.setName(post.getUser().getName());
        postTest.setUser(userTestForPostTest);
        postTest.setTitle(post.getTitle());
        String tempText = Jsoup.parse(post.getText()).text();
        if (tempText.length() > 150) {
            String announce = tempText.substring(0, 150);
            postTest.setAnnounce(announce.substring(0, announce.lastIndexOf(" ")) + "...");
        } else {
            postTest.setAnnounce(post.getText());
        }
        postTest.setLikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 1).count());
        postTest.setDislikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 0).count());
        postTest.setCommentCount(post.getPostComments().size());
        postTest.setViewCount(post.getViewCount());
        return postTest;
    }

    public PostResponse getPostModeration(int limit, int offset, String status) {

        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (userInDB.isIs_moderator() == 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        int modId = userInDB.getId();

        if (status.equalsIgnoreCase("NEW")) {
            return createResponse(postRepository.getNewPostsResponse(PageRequest.of(offset / limit, limit)));
        }
        if (status.equalsIgnoreCase("DECLINED")) {
            return createResponse(postRepository.getDeclinedPostsResponse(PageRequest.of(offset / limit, limit), modId));
        }
        if (status.equalsIgnoreCase("ACCEPTED")) {
            return createResponse(postRepository.getAcceptedPostsResponse(PageRequest.of(offset / limit, limit), modId));
        }
        return null;
    }

    private PostResponse createResponse(Page<Post> postsToShow) {
        PostResponse response = new PostResponse();
        List<PostTest> posts = new ArrayList<>();
        int postCount = (int) postsToShow.getTotalElements();
        for (Post post : postsToShow) {
            PostTest postTest = newPostTest(post);
            if (!posts.contains(postTest)) {
                posts.add(postTest);
            }
        }
        response.setCount(postCount);
        response.setPosts(posts);
        return response;
    }

    public PostResponse getMyPostsResponse(int limit, int offset, String status) {
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        int userId = userInDB.getId();
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (status.equalsIgnoreCase("inactive")) {
            return createResponse(postRepository.findMyInActivePosts(PageRequest.of(offset / limit, limit), userId));
        }
        if (status.equalsIgnoreCase("pending")) {
            return createResponse(postRepository.findMyPendingPosts(PageRequest.of(offset / limit, limit), userId));
        }
        if (status.equalsIgnoreCase("declined")) {
            return createResponse(postRepository.findMyDeclinedPosts(PageRequest.of(offset / limit, limit), userId));
        }
        if (status.equalsIgnoreCase("published")) {
            return createResponse(postRepository.findMyAcceptedPosts(PageRequest.of(offset / limit, limit), userId));
        }
        return null;
    }

    public static void checkAuthorized(String email) {
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public FailResponse postNewPost(NewPostRequest request) {
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (request.getTitle().length() < 3) {
            errors.put("title", "Заголовок не установлен");
        }
        if (request.getText().length() < 50) { // todo: распарсить текст, а потом проверить длину
            errors.put("text", "Текст публикации слишком короткий");
        }
        if (!errors.isEmpty()) {
            response.setResult(false);
            response.setErrors(errors);
            return response;
        } else {
            Post post = new Post();
            post.setIs_active(request.getActive());
            post.setModeration_status(ModerationStatus.NEW);
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

            Set<String> tagsFromRequest = request.getTags();

            for (String tag : tagsFromRequest) {
                Tags fromDB = tagsRepository.findTagByName(tag);
                if (fromDB == null) {
                    Tags newTag = new Tags();
                    newTag.setName(tag);
                    tagsRepository.save(newTag);
                }
            }
            for (String tag : tagsFromRequest) {
                Tags tagFromDB = tagsRepository.findTagByName(tag);
                createNewTag2Post(tagFromDB, post);
            }
        }
        response.setResult(true);
        return response;
    }

    private void createNewTag2Post(Tags tag, Post post) {
        Tag2Post tag2Post = new Tag2Post();
        tag2Post.setTag(tag);
        tag2Post.setPost(post);
        tag2PostRepository.save(tag2Post);
    }

    public FailResponse putPost(int id, NewPostRequest request) {
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (request.getTitle().length() < 3) {
            errors.put("title", "Заголовок не установлен");
        }
        if (request.getText().length() < 50) { // todo: распарсить текст, а потом проверить длину
            errors.put("text", "Текст публикации слишком короткий");
        }
        if (!errors.isEmpty()) {
            response.setResult(false);
            response.setErrors(errors);
            return response;
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
            postRepository.save(post);

            Set<String> tagsFromRequest = request.getTags();

            for (String tag : tagsFromRequest) {
                Tags fromDB = tagsRepository.findTagByName(tag);
                if (fromDB == null) {
                    Tags newTag = new Tags();
                    newTag.setName(tag);
                    tagsRepository.save(newTag);
                }
            }
            for (String tag : tagsFromRequest) {
                Tags tagFromDB = tagsRepository.findTagByName(tag);
                Tag2Post tag2Post = tag2PostRepository.findByPostAndTag(post.getId(), tagFromDB.getId());
                if (tag2Post == null) {
                    createNewTag2Post(tagFromDB, post);
                }
            }
            response.setResult(true);
            return response;
        }
    }

    public FailResponse postModeration(PostModerationRequest request) {
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        FailResponse response = new FailResponse();
        User moderator = findModeratorByEmail(findEmail);
        if (moderator == null) {
            response.setResult(false);
            return response;
        } else {
            if (request == null) {
                response.setResult(false);
                return response;
            }
            Post postToModerate = postRepository.getPostByIdModerate(request.getPostId());
            postToModerate.setModeration_status(request.getDecision().equals("accept")
                    ? ModerationStatus.ACCEPTED : ModerationStatus.DECLINED);
            postToModerate.setModerator_id(moderator.getId());
            postRepository.save(postToModerate);
            response.setResult(true);
            return response;
        }
    }

    private User findModeratorByEmail(String email) {
        return userRepository.findModeratorByEmail(email);
    }
}
