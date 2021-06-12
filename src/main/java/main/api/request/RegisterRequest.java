package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import main.config.Config;

import javax.validation.constraints.*;

public class RegisterRequest {

    @JsonProperty("e_mail")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    @Schema(description = "e-mail пользователя", example = "konstantin@mail.ru")
    private String email;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 255, message = Config.STRING_AUTH_PASSWORD)
    @Schema(description = "пароль для аккаунта", example = "123456")
    private String password;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 3, max = 20, message = Config.STRING_AUTH_NAME)
    @Schema(description = "имя пользователя", example = "Константин")
    private String name;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Schema(description = "код капчи", example = "d34f")
    private String captcha;

    @JsonProperty("captcha_secret")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Schema(description = "секретный код капчи", example = "69sdFd67df7Pd9d3")
    private String secretCode;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }
}
