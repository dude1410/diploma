package main.testEntity;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class CommentTestForPost {
    private int id;
    private Timestamp timestamp;
    private String text;
    private UserTestForCommentForPost user;

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
