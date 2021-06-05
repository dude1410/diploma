package main.service;

import main.api.request.MyProfileRequest;
import main.api.response.FailResponse;
import main.model.User;
import main.repository.UserRepository;
import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import static main.service.PostService.checkAuthorized;

@Service
public class MyProfileService {

    private final long MAX_IMAGE_SIZE = 5242880;

    private final UserRepository userRepository;
    private final FilesService filesService;

    public MyProfileService(UserRepository userRepository,
                            FilesService filesService) {
        this.userRepository = userRepository;
        this.filesService = filesService;
    }

    public FailResponse postMyProfileNewPhoto(MyProfileRequest request, BindingResult error) throws IOException {

        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        User userInDB = userRepository.findByEmail(findEmail);
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        if (userInDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!request.getName().isEmpty()) {
            userInDB.setName(request.getName());
        } else {
            errors.put("name", "Имя указано неверно");
        }
        if (request.getPassword() != null) {
            if (request.getPassword().length() < 6) {
                errors.put("password", "Пароль короче 6-ти символов");
            } else {
                userInDB.setPassword(passwordEncoder().encode(request.getPassword()));
            }
        }
        if (request.getRemovePhoto() != null) {
            if (request.getRemovePhoto() == 1) {
                userInDB.setPhoto("");
            } else {
                if (request.getPhoto().isEmpty()) {
                    errors.put("photo", "Фото отсутствует");
                }
                if (request.getPhoto().getSize() > MAX_IMAGE_SIZE) {
                    errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
                }
                if (!request.getPhoto().isEmpty() && request.getPhoto().getSize() < MAX_IMAGE_SIZE) {
                    BufferedImage bufferedImage = ImageIO.read(request.getPhoto().getInputStream());
                    BufferedImage image = Scalr.resize(bufferedImage, 36, 36);
                    String fileName = request.getPhoto().getOriginalFilename();
                    userInDB.setPhoto(filesService.cloudStore(image, fileName));
                }
            }
        }
        userRepository.save(userInDB);
        if (errors.isEmpty()) {
            response.setResult(true);
        } else {
            response.setResult(false);
            response.setErrors(errors);
        }
        if (request.getPassword() != null && !errors.containsKey("password")) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return response;
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
