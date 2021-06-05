package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.config.Config;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ChangePasswordRequest {

    private String code;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    private String password;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private String captcha;

    @JsonProperty("captcha_secret")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
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
