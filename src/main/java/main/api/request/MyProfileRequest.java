package main.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import main.config.Config;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class MyProfileRequest {

    @Size(min = 3, max = 20, message = Config.STRING_AUTH_NAME)
    @Schema(description = "новое имя", example = "Sendel")
    private String name;

    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    @Schema(description = "новый e-mail", example = "sndl@mail.ru")
    private String email;

    @Size(min = 6, max = 255, message = Config.STRING_AUTH_PASSWORD)
    @Schema(description = "новый пароль", example = "123456")
    private String password;

    @Schema(description = "параметр, который указывает на то, что фотографию нужно удалить (если\n" +
            "значение равно 1)", example = "0")
    private Integer removePhoto;

    @Schema(description = "файл с фото или пустое значение (если его требуется удалить)", example = "<binary_file>")
    private MultipartFile photo;

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

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
