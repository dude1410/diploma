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
        postResponse.setCount(2);
        List <PostTest> posts = new ArrayList<>();
        PostTest post1 = new PostTest();
        post1.setId(345);
        post1.setTimestamp(new Timestamp(System.currentTimeMillis()));
        UserTestForPostTest user1 = new UserTestForPostTest();
        user1.setId(88);
        user1.setName("Dmitriy Petrov");
        post1.setUser(user1);
        post1.setTitle("Text title");
        post1.setAnnounce("Text announce without html-tags");
        post1.setLikeCount(36);
        post1.setDislikeCount(3);
        post1.setCommentCount(15);
        post1.setViewCount(55);
        posts.add(post1);

        PostTest post2 = new PostTest();
        post2.setId(346);
        post2.setTimestamp(new Timestamp(System.currentTimeMillis()));
        UserTestForPostTest user2 = new UserTestForPostTest();
        user2.setId(89);
        user2.setName("Ivan Ivanov");
        post2.setUser(user2);
        post2.setTitle("Text title 2");
        post2.setAnnounce("Text announce without html-tags 2");
        post2.setLikeCount(37);
        post2.setDislikeCount(4);
        post2.setCommentCount(16);
        post2.setViewCount(56);
        posts.add(post2);

        postResponse.setPosts(posts);

        return postResponse;
    }

    public static PostResponse getPostEmptyResponse () {
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(0);
        List <PostTest> posts = new ArrayList<>();
        postResponse.setPosts(posts);

        return postResponse;
    }
}
