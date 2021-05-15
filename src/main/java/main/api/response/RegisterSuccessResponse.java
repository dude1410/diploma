package main.api.response;

import org.springframework.stereotype.Component;

@Component
public class RegisterSuccessResponse {

    private boolean result = true;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
