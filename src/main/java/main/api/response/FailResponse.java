package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import main.model.User;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FailResponse {

    private int id;
    private boolean result;
    private HashMap <String, String> errors;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, String> errors) {
        this.errors = errors;

        User user = new User();
    }
}
