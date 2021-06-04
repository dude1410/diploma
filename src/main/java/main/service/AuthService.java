package main.service;

import com.github.cage.Cage;
import com.github.cage.YCage;
import main.api.request.ChangePasswordRequest;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestorePasswordRequest;
import main.api.response.*;
import main.config.MailConfig;
import main.model.CaptchaCodes;
import main.model.User;
import main.repository.CaptchaCodesRepository;
import main.repository.GlobalSettingsRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    @Value("${blog.deleteCaptchaTimePeriod}")
    private String deleteCaptchaTimePeriod;
    private Cage cage;
    StringBuilder secretCode = new StringBuilder();
    StringBuilder captchaBaseCode = new StringBuilder();
    private final String CAPTCHA_BASE_CODE_PREFIX = "data:image/png;base64, ";
    private final String CAPTCHA_CODE_SYMBOLS = "abcdefghijklmnopqrstuvwxyz1234567890";
    private final String CAPTCHA_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private final int HASH_LENGTH = 45;
    private final String HASH_PREFIX = "http://localhost:8080/login/change-password/";
    private final String CHANGE_PASSWORD_ADRESS = "http://localhost:8080/auth/restore"; // todo: change to herokuapp
    private final long CODE_LIFETIME = 86400000L;
    private final int MIN_PASSWORD_LENGTH = 6;


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CaptchaCodesRepository captchaCodesRepository;
    private final MailConfig mailConfig;
    private final JavaMailSender javaMailSender;
    private final GlobalSettingsRepository globalSettingsRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PostRepository postRepository,
                       CaptchaCodesRepository captchaCodesRepository,
                       MailConfig mailConfig,
                       JavaMailSender javaMailSender,
                       GlobalSettingsRepository globalSettingsRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.captchaCodesRepository = captchaCodesRepository;
        this.mailConfig = mailConfig;
        this.javaMailSender = javaMailSender;
        this.globalSettingsRepository = globalSettingsRepository;
    }

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        var currentUser = userRepository.findByEmail(loginRequest.getEmail());
        if (currentUser == null) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setResult(false);
            return ResponseEntity.ok(loginResponse);
        }
        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(getLoginResponse(currentUser));
    }

    private LoginResponse getLoginResponse(User currentUser) {
        var loginResponse = new LoginResponse();
        var userLoginResponse = new UserLoginResponse();
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

    public ResponseEntity<LoginResponse> check() {

        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (findEmail == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(getLoginResponse(userInDB));
    }

    public CaptchaResponse getCaptcha() throws IOException {
        cage = new YCage();
        Date dateToDeleteCaptcha = new Date(new Date().getTime() - (Long.parseLong(deleteCaptchaTimePeriod) * 1000));
        captchaCodesRepository.deleteAllByTimeBefore(dateToDeleteCaptcha);

        captchaBaseCode = new StringBuilder();
        captchaBaseCode.append(CAPTCHA_BASE_CODE_PREFIX);

        String genCaptcha = generateNewCaptcha();

        BufferedImage bufferedImage = cage.drawImage(genCaptcha);
        captchaBaseCode.append(createCaptchaString(bufferedImage));
        CaptchaCodes captchaCodes = new CaptchaCodes();
        captchaCodes.setCode(genCaptcha);
        captchaCodes.setTime(new Timestamp(System.currentTimeMillis()));
        captchaCodes.setSecretCode(secretCode.toString());
        captchaCodesRepository.save(captchaCodes);
        return new CaptchaResponse(secretCode.toString(), captchaBaseCode.toString());
    }

    public String generateNewCaptcha() {
        secretCode = new StringBuilder();

        StringBuilder bufferCaptcha = new StringBuilder();
        Random random = new Random();
        int codeLength = 15 + (int) (Math.random() * 10);
        for (int i = 0; i < codeLength; i++) {
            int index = (int) (random.nextFloat() * CAPTCHA_CODE_SYMBOLS.length());
            secretCode.append(CAPTCHA_CODE_SYMBOLS.charAt(index));
        }
        int captchaLength = 4;
        while (bufferCaptcha.length() < captchaLength) {
            int index = (int) (random.nextFloat() * CAPTCHA_SYMBOLS.length());
            bufferCaptcha.append(CAPTCHA_SYMBOLS.charAt(index));
        }
        return bufferCaptcha.toString();
    }

    private String createCaptchaString(BufferedImage bufferedImage) throws IOException {
        BufferedImage resizedImage = (resizeImage(bufferedImage));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "png", baos);
        byte[] bytes = baos.toByteArray();
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

    public ResponseEntity<LogoutResponse> logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setResult(true);
        return ResponseEntity.ok(logoutResponse);
    }

    public FailResponse register(RegisterRequest request) throws IOException {

        if (globalSettingsRepository.findMultiuserMode().equals("NO")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User userInDB = userRepository.findByEmail(request.getEmail());
        if (userInDB != null) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (!request.getName().matches("([А-Яа-яA-Za-z0-9-_]+)")) {
            errors.put("name", "Имя указано неверно");
        }
        if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        CaptchaCodes captcha = captchaCodesRepository.findBySecretCode(request.getSecretCode());
        if (!captcha.getCode().equals(request.getCaptcha())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }
        if (errors.isEmpty()) {
            response.setResult(true);

            User user = new User();
            user.setIs_moderator((byte) 0);
            user.setReg_time(new Timestamp(System.currentTimeMillis()));
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder().encode(request.getPassword()));
            userRepository.save(user);
            return response;
        } else {
            response.setResult(false);
            response.setErrors(errors);
            return response;
        }
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    public FailResponse restorePassword (RestorePasswordRequest request) throws MessagingException {
        FailResponse response = new FailResponse();
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            response.setResult(false);
            return response;
        } else {
            StringBuilder hashBuilder = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < HASH_LENGTH; i++) {
                int index = (int) (random.nextFloat() * CAPTCHA_CODE_SYMBOLS.length());
                hashBuilder.append(CAPTCHA_CODE_SYMBOLS.charAt(index));
            }
            String userCode = hashBuilder.append(new Date().getTime()).toString();
            user.setCode(userCode);
            userRepository.save(user);

            MimeMessage message = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "utf-8");
            String messageText = HASH_PREFIX + userCode;
            String htmlMsg = "<a href=\"" + messageText + "\">Follow the link to change the password on the site</a>";
            message.setContent(htmlMsg, "text/html");
            helper.setTo(request.getEmail());
            helper.setSubject("Восстановление пароля");
            this.javaMailSender.send(message);
            response.setResult(true);
            return response;
        }
    }

    public FailResponse changePassword (ChangePasswordRequest request) {
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        User user = userRepository.findByCode(request.getCode());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (new Date().getTime() - Long.parseLong(user.getCode().substring(45)) > CODE_LIFETIME) {
            errors.put("code","Ссылка для восстановления пароля устарела.\n" +
                    "<a href=\"" + CHANGE_PASSWORD_ADRESS + ">Запросить ссылку снова</a>\"");
            response.setResult(false);
            response.setErrors(errors);
            return response;
        }
        String captchaSecret = request.getCaptchaSecret();
        CaptchaCodes captcha = captchaCodesRepository.findBySecretCode(captchaSecret);
        if (captcha == null) {
            errors.put("captcha","Код с картинки введён неверно");
            response.setResult(false);
            response.setErrors(errors);
            return response;
        }
        if (!captcha.getCode().equals(request.getCaptcha())){
            errors.put("captcha","Код с картинки введён неверно");
            response.setResult(false);
            response.setErrors(errors);
            return response;
        }
        if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            errors.put("password","Пароль короче 6-ти символов");
            response.setResult(false);
            response.setErrors(errors);
            return response;
        }
        user.setPassword(passwordEncoder().encode(request.getPassword()));
        userRepository.save(user);
        response.setResult(true);
        return response;
    }
}
