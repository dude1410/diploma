package main.service;

import main.api.request.MyProfileRequest;
import main.api.request.MyProfileRequestNoPhoto;
import main.api.response.FailResponse;
import main.model.User;
import main.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import static main.service.PostService.checkAuthorized;

@Service
public class MyProfileService {

    private final Logger logger = LogManager.getLogger(MyProfileService.class);

    private final long MAX_IMAGE_SIZE = 5242880;

    private final UserRepository userRepository;
    private final FilesService filesService;

    public MyProfileService(UserRepository userRepository,
                            FilesService filesService) {
        this.userRepository = userRepository;
        this.filesService = filesService;
    }

    public FailResponse postMyProfileNewPhoto(MyProfileRequest request,
                                              BindingResult error) throws IOException {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during logging = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info("Начало проверки авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Проверка авторизации пройдена успешно");
        User userInDB = userRepository.findByEmail(findEmail);
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        if (userInDB == null) {
            logger.error("Пользователь с почтовым ящиком " + request.getEmail() + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!request.getName().isEmpty()) {
            userInDB.setName(request.getName());
        } else {
            logger.error("Имя пользователя не указано");
            errors.put("name", "Имя указано неверно");
        }
        if (request.getPassword() != null) {
            if (request.getPassword().length() < 6) {
                logger.error("Пароль должен быть не короче 6 символов");
                errors.put("password", "Пароль короче 6-ти символов");
            } else {
                String newPassword = passwordEncoder().encode(request.getPassword());
                userInDB.setPassword(newPassword);
                logger.info("Пользователь изменил пароль на " + newPassword);
            }
        }
        if (request.getRemovePhoto() != null) {
            if (request.getRemovePhoto() == 1) {
                logger.info("Пользователь удалил свой аватар");
                userInDB.setPhoto("");
            } else {
                if (request.getPhoto().isEmpty()) {
                    logger.error("Фото отсутствует");
                    errors.put("photo", "Фото отсутствует");
                }
                if (request.getPhoto().getSize() > MAX_IMAGE_SIZE) {
                    logger.error("Фото слишком большое, нужно не более 5 Мб");
                    errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
                }
                if (!request.getPhoto().isEmpty() && request.getPhoto().getSize() < MAX_IMAGE_SIZE) {
                    BufferedImage bufferedImage = ImageIO.read(request.getPhoto().getInputStream());
                    BufferedImage image = Scalr.resize(bufferedImage, 36, 36);
                    String fileName = request.getPhoto().getOriginalFilename();
                    userInDB.setPhoto(filesService.cloudStore(image, fileName));
                    logger.info("Пользователь установил новый аватар");
                }
            }
        }
        userRepository.save(userInDB);
        if (errors.isEmpty()) {
            logger.info("Изменения профиля " + userInDB.getName() + " прошли успешно");
            response.setResult(true);
        } else {
            logger.error("Изменения профиля " + userInDB.getName() + " не удались из-за возникших ошибок");
            response.setResult(false);
            response.setErrors(errors);
        }
        if (request.getPassword() != null && !errors.containsKey("password")) {
            logger.info("Пользователь изменил пароль. Нужно заново залогиниться.");
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return response;
    }

    public FailResponse postMyProfileNoPhoto(MyProfileRequestNoPhoto request,
                                             BindingResult error) throws IOException {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during logging = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info("Начало проверки авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Проверка авторизации пройдена успешно");
        User userInDB = userRepository.findByEmail(findEmail);
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        if (userInDB == null) {
            logger.error("Пользователь с почтовым ящиком " + request.getEmail() + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!request.getName().isEmpty()) {
            userInDB.setName(request.getName());
        } else {
            logger.error("Имя пользователя не указано");
            errors.put("name", "Имя указано неверно");
        }
        if (request.getPassword() != null) {
            if (request.getPassword().length() < 6) {
                logger.error("Пароль должен быть не короче 6 символов");
                errors.put("password", "Пароль короче 6-ти символов");
            } else {
                String newPassword = passwordEncoder().encode(request.getPassword());
                userInDB.setPassword(newPassword);
                logger.info("Пользователь изменил пароль на " + newPassword);
            }
        }
        if (request.getRemovePhoto() != null) {
            if (request.getRemovePhoto() == 1) {
                logger.info("Пользователь удалил свой аватар");
                userInDB.setPhoto("");
            }
        }
        userRepository.save(userInDB);
        if (errors.isEmpty()) {
            logger.info("Изменения профиля " + userInDB.getName() + " прошли успешно");
            response.setResult(true);
        } else {
            logger.error("Изменения профиля " + userInDB.getName() + " не удались из-за возникших ошибок");
            response.setResult(false);
            response.setErrors(errors);
        }
        if (request.getPassword() != null && !errors.containsKey("password")) {
            logger.info("Пользователь изменил пароль. Нужно заново залогиниться.");
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return response;
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
