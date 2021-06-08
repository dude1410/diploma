package main.service;

import com.github.cage.Cage;
import com.github.cage.YCage;
import io.swagger.annotations.ApiOperation;
import main.api.request.ChangePasswordRequest;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestorePasswordRequest;
import main.api.response.*;
import main.config.Config;
import main.model.CaptchaCodes;
import main.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import main.repository.CaptchaCodesRepository;
import main.repository.GlobalSettingsRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

@Service
public class AuthService {

    StringBuilder secretCode = new StringBuilder();
    StringBuilder captchaBaseCode = new StringBuilder();

//    private final String HASH_PREFIX = "http://localhost:8080/login/change-password/"; // address for local start
    private final String HASH_PREFIX = "https://pavel-vladysik-blog-core-on-ja.herokuapp.com/login/change-password/";
//    private final String CHANGE_PASSWORD_ADDRESS = "http://localhost:8080/auth/restore"; // address for local start
    private final String CHANGE_PASSWORD_ADDRESS = "https://pavel-vladysik-blog-core-on-ja.herokuapp.com/auth/restore";
    private final int MIN_PASSWORD_LENGTH = 6;

    private final Logger logger = LogManager.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CaptchaCodesRepository captchaCodesRepository;
    private final JavaMailSender javaMailSender;
    private final GlobalSettingsRepository globalSettingsRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PostRepository postRepository,
                       CaptchaCodesRepository captchaCodesRepository,
                       JavaMailSender javaMailSender,
                       GlobalSettingsRepository globalSettingsRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.captchaCodesRepository = captchaCodesRepository;
        this.javaMailSender = javaMailSender;
        this.globalSettingsRepository = globalSettingsRepository;
    }

    // вход на сайт зарегистрированного пользователя

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest,
                                               BindingResult error) {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during logging = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User currentUser = userRepository.findByEmail(loginRequest.getEmail());
        if (currentUser == null) {
            logger.error("Пользователь с почтовым ящиком " + loginRequest.getEmail() + " не найден");
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setResult(false);
            return ResponseEntity.ok(loginResponse);
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("Пользователь с почтой " + currentUser.getEmail() + ", именем " + currentUser.getName() +
                " и паролем " + currentUser.getPassword() + " вошел в систему");
        return ResponseEntity.ok(getLoginResponse(currentUser));
    }

    // проверка авторизации

    public ResponseEntity<LoginResponse> check() {

        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (findEmail == null){
            logger.info("Проверка авторизации - пользователь не авторизован");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            logger.info("Проверка авторизации - пользователь с почтой " + findEmail + " не найден в базе данных");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        logger.info("Проверка авторизации - пользователь с почтой " + findEmail + " найден в базе данных и авторизован");
        return ResponseEntity.ok(getLoginResponse(userInDB));
    }

    // получение капчи

    public CaptchaResponse getCaptcha() throws IOException {
        Cage cage = new YCage();
        Date dateToDeleteCaptcha = new Date(new Date().getTime() - (long) (Config.DELETE_CAPTCHA_TIME_PERIOD) * 1000);
        logger.info("Удаление из базы данных всех капчей старше заданного параметра");
        captchaCodesRepository.deleteAllByTimeBefore(dateToDeleteCaptcha);
        captchaBaseCode = new StringBuilder();
        captchaBaseCode.append(Config.CAPTCHA_BASE_CODE_PREFIX);

        String genCaptcha = generateNewCaptcha();

        BufferedImage bufferedImage = cage.drawImage(genCaptcha);
        captchaBaseCode.append(createCaptchaString(bufferedImage));
        CaptchaCodes captchaCodes = new CaptchaCodes();
        captchaCodes.setCode(genCaptcha);
        captchaCodes.setTime(new Timestamp(System.currentTimeMillis()));
        captchaCodes.setSecretCode(secretCode.toString());
        captchaCodesRepository.save(captchaCodes);
        logger.info("Капча получена");
        return new CaptchaResponse(secretCode.toString(), captchaBaseCode.toString());
    }

    // разлогиниться

    public ResponseEntity<LogoutResponse> logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setResult(true);
        logger.info("Пользователь заканчивает сессию");
        return ResponseEntity.ok(logoutResponse);
    }

    // метод регистрации нового пользователя

    public FailResponse register(RegisterRequest request,
                                 BindingResult error) throws IOException {

        if (error.hasErrors()) {
            logger.error(String.format("Errors during registration = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (globalSettingsRepository.findMultiuserMode().equals("NO")) {
            logger.error("Многопользовательский режим отключен - неудачная попытка регистрации");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        if (request == null) {
            logger.error("С фронта пришел пустой запрос при попытке регистрации");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info("С фронта приходит запрос - начало проверки данных их запроса");
        User userInDB = userRepository.findByEmail(request.getEmail());
        if (userInDB != null) {
            logger.error("Ошибка регистрации - пользователь с почтовым ящиком " + request.getEmail() + " уже зарегистрирован");
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        logger.info("Пользователя с такой почтой не существует в базе - продолжение регистрации");
        if (!request.getName().matches("([А-Яа-яA-Za-z0-9-_]+)")) {
            logger.error("Ошибка регистрации - " + request.getName() + "неверный формат ввода имени пользователя");
            errors.put("name", "Имя указано неверно");
        }
        logger.info("Имя пользователя введено кооректно - продолжение регистрации");
        if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            logger.error("Ошибка регистрации - Пароль короче 6-ти символов");
            errors.put("password", "Пароль короче 6-ти символов");
        }
        logger.info("Пароль прошел валидацию - продолжение регистрации");
        CaptchaCodes captcha = captchaCodesRepository.findBySecretCode(request.getSecretCode());
        if (!captcha.getCode().equals(request.getCaptcha())) {
            logger.error("Ошибка регистрации - Код с картинки введён неверно");
            errors.put("captcha", "Код с картинки введён неверно");
        }
        logger.info("Код с картинки введен корректно - продолжение регистрации");
        if (errors.isEmpty()) {
            response.setResult(true);
            User user = new User();
            user.setIs_moderator((byte) 0);
            user.setReg_time(new Timestamp(System.currentTimeMillis()));
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder().encode(request.getPassword()));
            userRepository.save(user);
            logger.info("Успешная попытка регистрации. Пользователь с почтой " + request.getEmail() +
                    " паролем " + user.getPassword() + " и именем " + request.getName() + " был зарегистрирован " +
                    user.getReg_time());
        } else {
            logger.info("Неуспешная попытка регистрации");
            response.setResult(false);
            response.setErrors(errors);
        }
        return response;
    }

    // восстановление пароля

    public FailResponse restorePassword (RestorePasswordRequest request,
                                         BindingResult error) throws MessagingException {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during registration = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (request == null) {
            logger.error("С фронта пришел пустой запрос");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info("С фронта получен запрос - начало восстановления пароля");
        FailResponse response = new FailResponse();
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            logger.error("Ошибка восстановления пароля - пользователь с ящиком " + request.getEmail() + " не найден");
            response.setResult(false);
        } else {
            logger.info("Восстановление пароля - пользователь с ящиком " + request.getEmail() + " найден");
            StringBuilder hashBuilder = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < Config.HASH_LENGTH; i++) {
                int index = (int) (random.nextFloat() * Config.CAPTCHA_CODE_SYMBOLS.length());
                hashBuilder.append(Config.CAPTCHA_CODE_SYMBOLS.charAt(index));
            }
            logger.info("Хэш для ссылки на восстановление пароля сгенерирован");
            String userCode = hashBuilder.append(new Date().getTime()).toString();
            user.setCode(userCode);
            userRepository.save(user);
            logger.info("В базу данных сохранены данные для восстановления пароля");

            MimeMessage message = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "utf-8");
            String messageText = HASH_PREFIX + userCode;
            String htmlMsg = "<a href=\"" + messageText + "\">Follow the link to change the password on the site</a>";
            message.setContent(htmlMsg, "text/html");
            helper.setTo(request.getEmail());
            helper.setSubject("Восстановление пароля");
            this.javaMailSender.send(message);
            logger.info("На ящик " + request.getEmail() + " направленна ссылка на соввстановление пароля");
            response.setResult(true);
        }
        return response;
    }

    // смена пароля

    public FailResponse changePassword (ChangePasswordRequest request,
                                        BindingResult error) {
        if (error.hasErrors()) {
            logger.error(String.format("Errors during registration = '%s'", error.getAllErrors()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (request == null) {
            logger.error("С фронта пришел пустой запрос - смена пароля неуспешна");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info("С фронта пришел запрос - начало процедуры смены пароля");
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        User user = userRepository.findByCode(request.getCode());
        if (user == null) {
            logger.error("Ошибка смены пароля - пользователь с кодом на восстановление " + request.getCode() + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (new Date().getTime() - Long.parseLong(user.getCode().substring(45)) > Config.CODE_LIFETIME) {
            logger.info("Ошибка смены пароля - Ссылка для восстановления пароля устарела.");
            errors.put("code","Ссылка для восстановления пароля устарела.\n" +
                    "<a href=\"" + CHANGE_PASSWORD_ADDRESS + ">Запросить ссылку снова</a>\"");
            response.setResult(false);
            response.setErrors(errors);
            return response;
        }
        CaptchaCodes captcha = captchaCodesRepository.findBySecretCode(request.getCaptchaSecret());
        if (captcha == null) {
            logger.error("Ошибка смены пароля - Капча не введена");
            errors.put("captcha","Код с картинки введён неверно");
            response.setResult(false);
            response.setErrors(errors);
            return response;
        }
        if (!captcha.getCode().equals(request.getCaptcha())){
            logger.error("Ошибка смены пароля - Код с картинки введён неверно");
            errors.put("captcha","Код с картинки введён неверно");
            response.setResult(false);
            response.setErrors(errors);
            return response;
        }
        if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            logger.error("Ошибка смены пароля - Пароль короче 6-ти символов");
            errors.put("password","Пароль короче 6-ти символов");
            response.setResult(false);
            response.setErrors(errors);
            return response;
        }
        user.setPassword(passwordEncoder().encode(request.getPassword()));
        userRepository.save(user);
        logger.info("Пользователь " + user.getName() + "и почтовым ящиком " + user.getEmail() + " сменил пароль на " +
                user.getPassword());
        response.setResult(true);
        return response;
    }

    // энкодер для пароля

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(Config.INT_AUTH_BCRYPT_STRENGTH);
    }

    private LoginResponse getLoginResponse(User currentUser) {
        LoginResponse loginResponse = new LoginResponse();
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setEmail(currentUser.getEmail());
        userLoginResponse.setModeration(currentUser.isIs_moderator() == 1);
        int newPosts = postRepository.findNewPosts();
        userLoginResponse.setModerationCount(currentUser.isIs_moderator() == 1 ? newPosts : 0);
        userLoginResponse.setId(currentUser.getId());
        userLoginResponse.setName(currentUser.getName());
        userLoginResponse.setPhoto(currentUser.getPhoto());
        userLoginResponse.setSettings(currentUser.isIs_moderator() == 1);
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }

    private String generateNewCaptcha() {
        secretCode = new StringBuilder();
        StringBuilder bufferCaptcha = new StringBuilder();
        Random random = new Random();
        int codeLength = 15 + (int) (Math.random() * 10);
        for (int i = 0; i < codeLength; i++) {
            int index = (int) (random.nextFloat() * Config.CAPTCHA_CODE_SYMBOLS.length());
            secretCode.append(Config.CAPTCHA_CODE_SYMBOLS.charAt(index));
        }
        while (bufferCaptcha.length() < Config.INT_CAPTCHA_LENGTH) {
            int index = (int) (random.nextFloat() * Config.CAPTCHA_SYMBOLS.length());
            bufferCaptcha.append(Config.CAPTCHA_SYMBOLS.charAt(index));
        }
        logger.info("Новая капча сгенерирована длиной " + Config.INT_CAPTCHA_LENGTH + " символов/-а");
        return bufferCaptcha.toString();
    }

    private String createCaptchaString(BufferedImage bufferedImage) throws IOException {
        BufferedImage resizedImage = resizeImage(bufferedImage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "png", baos);
        byte[] bytes = baos.toByteArray();
        logger.info("Перевод капчи в строку");
        return Base64.getEncoder().encodeToString(bytes);
    }

    private BufferedImage resizeImage(BufferedImage bufferedImage) {
        Image temp = bufferedImage.getScaledInstance(100, 35, Image.SCALE_SMOOTH);
        BufferedImage img = new BufferedImage(100, 35, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = img.createGraphics();
        graphics2D.drawImage(temp, 0, 0, null);
        graphics2D.dispose();
        return img;
    }
}
