package main.service;

import main.api.request.CommentRequest;
import main.api.response.FailResponse;
import main.model.Post;
import main.model.PostComments;
import main.model.User;
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
import org.springframework.validation.Errors;
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

    public FailResponse postComment(CommentRequest request, BindingResult error) {

        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);

        FailResponse response = new FailResponse();
        int newPC;
        if (request.getText() == null) {
            return fillResponse(response);
        }
        String tempText = Jsoup.parse(request.getText()).text();
        if (tempText.length() < 3) {
            return fillResponse(response);
        }

        Post post = postRepository.getPostById(request.getPostId());
//        PostComments comment = postCommentsRepository.findById(request.getParentId());


        if (post != null) {
            if (request.getParentId() != null) { // ???
                newPC = createNewPostCommentWithParent(request.getParentId(),
                        post,
                        request.getText(),
                        findEmail);
                response.setId(newPC);
                return response;
            } else {
                newPC = createNewPostCommentWithoutParent(post,
                        request.getText(),
                        findEmail);
                response.setId(newPC);
                return response;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private FailResponse fillResponse(FailResponse response) {
        HashMap<String, String> errors = new HashMap<>();
        response.setResult(false);
        errors.put("text", "Текст комментария не задан или слишком короткий");
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
