package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.config.Config;

import javax.validation.constraints.*;

public class RegisterRequest {

    @JsonProperty("e_mail")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    private String email;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 255, message = Config.STRING_AUTH_PASSWORD)
    private String password;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 3, max = 20, message = Config.STRING_AUTH_NAME)
    private String name;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private String captcha;

    @JsonProperty("captcha_secret")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
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
