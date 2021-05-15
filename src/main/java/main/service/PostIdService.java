package main.service;

import main.api.response.PostResponseId;
import main.testEntity.CommentTestForPost;
import main.testEntity.UserTestForCommentForPost;
import main.testEntity.UserTestForPostTest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostIdService {

    public static PostResponseId getPostResponseId () {
        PostResponseId postResponseId = new PostResponseId();
        postResponseId.setId(34);
        postResponseId.setTimestamp(new Timestamp(System.currentTimeMillis()));
        postResponseId.setActive(true);
        UserTestForPostTest user = new UserTestForPostTest();
        user.setId(88);
        user.setName("Dmitriy Petrov");
        postResponseId.setUser(user);
        postResponseId.setTitle("Post title");
        postResponseId.setText("Post text in HTML format");
        postResponseId.setLikeCount(36);
        postResponseId.setDislikeCount(3);
        postResponseId.setViewCount(55);

        List<CommentTestForPost> comments = new ArrayList<>();

        CommentTestForPost comment1 = new CommentTestForPost();
        comment1.setId(776);
        comment1.setTimestamp(new Timestamp(System.currentTimeMillis()));
        comment1.setText("Comment text in HTML format");
        UserTestForCommentForPost user1 = new UserTestForCommentForPost();
        user1.setId(88);
        user1.setName("Dmitriy Petrov");
        user1.setPhoto("/avatars/ab/cd/ef/52461.jpg");
        comment1.setUser(user1);

        CommentTestForPost comment2 = new CommentTestForPost();
        comment2.setId(777);
        comment2.setTimestamp(new Timestamp(System.currentTimeMillis()));
        comment2.setText("Comment text in HTML format 2");
        UserTestForCommentForPost user2 = new UserTestForCommentForPost();
        user2.setId(89);
        user2.setName("Ivan Ivanov");
        user2.setPhoto("/avatars/ab/cd/ef/52462.jpg");
        comment2.setUser(user2);

        comments.add(comment1);
        comments.add(comment2);
        postResponseId.setComments(comments);

        List<String> tags = new ArrayList<>();
        tags.add("Articles");
        tags.add("Java");
        postResponseId.setTags(tags);

        return postResponseId;
    }
}
