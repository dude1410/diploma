package main.model.DTO;

import org.springframework.stereotype.Component;

@Component
public class CommentTestForPost {
    private int id;
    private long timestamp;
    private String text;
    private UserTestForCommentForPost user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserTestForCommentForPost getUser() {
        return user;
    }

    public void setUser(UserTestForCommentForPost user) {
        this.user = user;
    }
}