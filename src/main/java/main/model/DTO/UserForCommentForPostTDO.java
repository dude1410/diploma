package main.model.DTO;

import org.springframework.stereotype.Component;

@Component
public class UserForCommentForPostTDO {

    private int id = 576;
    private String name = "Dmitriy Petrov";
    private String photo = "/avatars/ab/cd/ef/52461.jpg";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
