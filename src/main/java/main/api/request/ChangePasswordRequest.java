package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import main.config.Config;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ChangePasswordRequest {

    @Schema(description = "Код восстановления пароля", example = "b55ca6ea6cb103c6384cfa366b7ce0bdcac092be26bc0")
    private String code;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    @Schema(description = "Новый пароль", example = "123456")
    private String password;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Schema(description = "Капча", example = "3166")
    private String captcha;

    @JsonProperty("captcha_secret")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Schema(description = "Секретный код капчи", example = "eqKIqurpZs")
    private String captchaSecret;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaSecret() {
        return captchaSecret;
    }

    public void setCaptchaSecret(String captchaSecret) {
        this.captchaSecret = captchaSecret;
    }
}
