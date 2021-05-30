package main.service;

import main.api.request.CommentRequest;
import main.api.response.FailResponse;
import main.model.Post;
import main.model.PostComments;
import main.repository.PostCommentsRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.HashMap;

import static main.service.PostService.checkAuthorized;

@Service
public class CommentService {

    private final PostCommentsRepository postCommentsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(PostCommentsRepository postCommentsRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postCommentsRepository = postCommentsRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public FailResponse postComment(CommentRequest request) {

        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // проверка авторизации
        checkAuthorized(findEmail);

        FailResponse response = new FailResponse();
        int newPC;
//        if (request.getPostId() == 0) { // пришел пустой postId, так нельзя
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
        if (request.getText() == null) { // пришел пустой текст, так нельзя
            return fillResponse(response);
        }
        String tempText = Jsoup.parse(request.getText()).text();
        if (tempText.length() < 10) { // пришел короткий текст, так нельзя
            return fillResponse(response);
        }
        if (request.getPostId() != 0 && request.getParentId() != 0) { // случай для коммента к коменту
            PostComments postComment = postCommentsRepository.
                    findPostCommentByPostAndParent(request.getPostId(), request.getParentId());
            if (postComment == null) { // нет в базе поста с комментом, к которому мы пишем коммент
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else { // есть такой пост с комментом, пишем коммент к комменту
                newPC = createNewPostCommentWithParent(request.getParentId(),
                        request.getPostId(),
                        request.getText(),
                        findEmail);
                response.setId(newPC);
                return response;
            }
        } else { // случай для коммента к посту
            Post post = postRepository.getPostById(request.getPostId());
            if (post == null) { // найти пост, к которому пишем коммент
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else { // есть такой пост, пишем коммент
                newPC = createNewPostCommentWithoutParent(request.getPostId(),
                        request.getText(),
                        findEmail);
            }
            response.setId(newPC);
            return response;
        }
    }

    private FailResponse fillResponse(FailResponse response) {
        HashMap<String, String> errors = new HashMap<>();
        response.setResult(false);
        errors.put("text", "Текст комментария не задан или слишком короткий");
        response.setErrors(errors);
        return response;
    }

    private int createNewPostCommentWithParent(int parentId, int postId, String text, String findEmail) {
        PostComments newPostComment = new PostComments();
        newPostComment.setParentId(parentId);
        newPostComment.setPost(postRepository.getPostById(postId));
        newPostComment.setUser(userRepository.findByEmail(findEmail));
        newPostComment.setTime(new Timestamp(System.currentTimeMillis()));
        newPostComment.setText(text);
        postCommentsRepository.save(newPostComment);
        return postCommentsRepository.findPostCommentIdByPostAndTextAndParent(postId, text, parentId);
    }

    private int createNewPostCommentWithoutParent(int postId, String text, String findEmail) {
        PostComments newPostComment = new PostComments();
        newPostComment.setPost(postRepository.getPostById(postId));
        newPostComment.setUser(userRepository.findByEmail(findEmail));
        newPostComment.setTime(new Timestamp(System.currentTimeMillis()));
        newPostComment.setText(text);
        postCommentsRepository.save(newPostComment);
        return postCommentsRepository.findPostCommentIdByPostAndText(postId, text);
    }
}
