package main.service;

import main.api.response.CaptchaResponse;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

    public static CaptchaResponse getCaptcha () {
        CaptchaResponse captchaResponse = new CaptchaResponse();
        captchaResponse.setSecret("car4y8cryaw84cr89awnrc");
        captchaResponse.setImage("data:image/png;base64, код_изображения_в_base64");
        return captchaResponse;
    }
}
