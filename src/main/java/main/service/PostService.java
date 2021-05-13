package main.service;

import main.api.response.PostResponse;
import main.testEntity.PostTest;
import main.testEntity.UserTestForPostTest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    public static PostResponse getPostResponse () {
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(1);
        List <PostTest> posts = new ArrayList<>();
        PostTest post1 = new PostTest();
        post1.setId(345);
        post1.setTimestamp(new Timestamp(System.currentTimeMillis()));
        UserTestForPostTest user = new UserTestForPostTest();
        user.setId(88);
        user.setName("Dmitriy Petrov");
        post1.setUser(user);
        post1.setTitle("Text title");
        post1.setAnnounce("Text announce without html-tags");
        post1.setLikeCount(36);
        post1.setDislikeCount(3);
        post1.setCommentCount(15);
        post1.setViewCount(55);
        posts.add(post1);
        postResponse.setPosts(posts);

        return postResponse;
    }
}
