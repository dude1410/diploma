package main.api.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    private boolean result;

    @JsonProperty("user")
    private UserLoginResponse userLoginResponse;

    public LoginResponse(boolean result, UserLoginResponse userLoginResponse) {
        this.result = result;
        this.userLoginResponse = userLoginResponse;
    }

    public LoginResponse() {
    }

    public LoginResponse(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public UserLoginResponse getUserLoginResponse() {
        return userLoginResponse;
    }

    public void setUserLoginResponse(UserLoginResponse userLoginResponse) {
        this.userLoginResponse = userLoginResponse;
    }
}
