package main.api.request;

import main.config.Config;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class MyProfileRequestNoPhoto {

    @Size(min = 3, max = 20, message = Config.STRING_AUTH_NAME)
    private String name;

    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    private String email;

    @Size(min = 6, max = 255, message = Config.STRING_AUTH_PASSWORD)
    private String password;

    private Integer removePhoto;
    private String photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRemovePhoto() {
        return removePhoto;
    }

    public void setRemovePhoto(Integer removePhoto) {
        this.removePhoto = removePhoto;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
