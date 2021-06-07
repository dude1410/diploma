package main.service;

import main.api.request.CommentRequest;
import main.api.response.FailResponse;
import main.config.Config;
import main.model.Post;
import main.model.PostComments;
import main.repository.PostCommentsRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.HashMap;

import static main.service.PostService.checkAuthorized;

@Service
public class CommentService {

    private final Logger logger = LogManager.getLogger(CommentService.class);

    private final PostCommentsRepository postCommentsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(PostCommentsRepository postCommentsRepository,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.postCommentsRepository = postCommentsRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public FailResponse postComment(CommentRequest request,
                                    BindingResult error) {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during logging = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info("Проверка авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Авторизация прошла успешно");
        if (request == null) {
            logger.error("Ошибка - С фронта пришел пустой запрос");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info("С фронта пришел запрос - начало составления ответа");
        FailResponse response = new FailResponse();
        int newPC;
        if (request.getText() == null) {
            logger.info(Config.STRING_COMMENT_IS_EMPTY_OR_SHORT);
            return fillResponse(response);
        }
        String tempText = Jsoup.parse(request.getText()).text();
        if (tempText.length() < 3) {
            logger.info(Config.STRING_COMMENT_IS_EMPTY_OR_SHORT);
            return fillResponse(response);
        }
        logger.info("Поиск поста в Базе данных по id " + request.getPostId());
        Post post = postRepository.getPostById(request.getPostId());

        if (post != null) {
            logger.info("Пост с id " + request.getPostId() + " найден");
            if (request.getParentId() != null) {
                newPC = createNewPostCommentWithParent(request.getParentId(),
                        post,
                        request.getText(),
                        findEmail);
                logger.info("Создан комментарий к комментарию " + request.getParentId());
            } else {
                newPC = createNewPostCommentWithoutParent(post,
                        request.getText(),
                        findEmail);
                logger.info("Создан комментарий к посту " + request.getPostId());
            }
            response.setId(newPC);
            logger.info("Новый комментарий имеет id " + newPC);
            return response;
        } else {
            logger.error("Пост с id " + request.getPostId() + " не найден");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private FailResponse fillResponse(FailResponse response) {
        HashMap<String, String> errors = new HashMap<>();
        response.setResult(false);
        errors.put("text", Config.STRING_COMMENT_IS_EMPTY_OR_SHORT);
        response.setErrors(errors);
        return response;
    }

    private int createNewPostCommentWithParent(int parentId, Post post, String text, String findEmail) {
        PostComments newPostComment = new PostComments();
        newPostComment.setParentId(parentId);
        newPostComment.setPost(post);
        newPostComment.setUser(userRepository.findByEmail(findEmail));
        newPostComment.setTime(new Timestamp(System.currentTimeMillis()));
        newPostComment.setText(text);
        return postCommentsRepository.save(newPostComment).getId();
    }

    private int createNewPostCommentWithoutParent(Post post, String text, String findEmail) {
        PostComments newPostComment = new PostComments();
        newPostComment.setUser(userRepository.findByEmail(findEmail));
        newPostComment.setTime(new Timestamp(System.currentTimeMillis()));
        newPostComment.setText(text);
        newPostComment.setPost(post);
        return postCommentsRepository.save(newPostComment).getId();
    }
}
