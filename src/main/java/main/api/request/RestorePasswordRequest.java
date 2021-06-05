package main.api.request;

import main.config.Config;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RestorePasswordRequest {

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
