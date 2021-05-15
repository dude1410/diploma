package main.service;

import main.api.response.RegisterFailResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RegisterFailService {

    public static RegisterFailResponse getResponse() {
        RegisterFailResponse failResponse = new RegisterFailResponse();
        failResponse.setResult(false);
        HashMap <String, String> errors = new HashMap<>();
        errors.put("email", "Этот e-mail уже зарегистрирован");
        errors.put("name", "Имя указано неверно");
        errors.put("password", "Пароль короче 6-ти символов");
        errors.put("captcha", "Код с картинки введён неверно" );
        failResponse.setErrors(errors);
        return failResponse;
    }
}
