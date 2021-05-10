package main.testEntity;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class PostTest {

    private int id = 345;
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private UserTestForPostTest user = new UserTestForPostTest();
    private String title = "Text title";
    private String announce = "Text announce without html-tags";
    private int likeCount = 36;
    private int dislikeCount = 3;
    private int commentCount = 15;
    private int viewCount = 55;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public UserTestForPostTest getUser() {
        return user;
    }

    public void setUser(UserTestForPostTest user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}