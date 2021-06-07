package main.model.DTO;

import org.springframework.stereotype.Component;

@Component
public class CommentForPostTDO {
    private int id;
    private long timestamp;
    private String text;
    private UserForCommentForPostTDO user;

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

    public UserForCommentForPostTDO getUser() {
        return user;
    }

    public void setUser(UserForCommentForPostTDO user) {
        this.user = user;
    }
}
