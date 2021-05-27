package main.service;

import com.github.cage.Cage;
import com.github.cage.YCage;
import main.api.request.LoginRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.LogoutResponse;
import main.api.response.UserLoginResponse;
import main.model.CaptchaCodes;
import main.model.User;
import main.repository.CaptchaCodesRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

@Service
public class AuthService {

    @Value("${blog.deleteCaptchaTimePeriod}")
    private String deleteCaptchaTimePeriod;
    private Cage cage;
    StringBuilder secretCode = new StringBuilder();
    StringBuilder captchaBaseCode = new StringBuilder();
    private final String captchaBaseCodePrefix = "data:image/png;base64, ";
    private final String captchaCodeSymbols = "abcdefghijklmnopqrstuvwxyz1234567890";
    private final String captchaSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CaptchaCodesRepository captchaCodesRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PostRepository postRepository,
                       CaptchaCodesRepository captchaCodesRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.captchaCodesRepository = captchaCodesRepository;
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
        userLoginResponse.setSettings(currentUser.isIs_moderator() == 1 ? true : false);
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }

    public ResponseEntity<LoginResponse> check(Principal principal) { // todo: откуда у нас principal?
        var currentUser = userRepository.findByEmail(principal.getName());
        return ResponseEntity.ok(getLoginResponse(currentUser));
    }

    public CaptchaResponse getCaptcha() throws IOException {
        cage = new YCage();
        Date dateToDeleteCaptcha = new Date(new Date().getTime() - (Long.parseLong(deleteCaptchaTimePeriod) * 1000));
        captchaCodesRepository.deleteAllByTimeBefore(dateToDeleteCaptcha);
        BufferedImage bufferedImage = cage.drawImage(generateNewCaptcha());
        captchaBaseCode.append(createCaptchaString(bufferedImage));
        CaptchaCodes captchaCodes = new CaptchaCodes();
        captchaCodes.setCode(captchaBaseCode.toString());
        System.out.println(captchaBaseCode.toString());
        captchaCodes.setTime(new Timestamp(System.currentTimeMillis()));
        System.out.println(new Timestamp(System.currentTimeMillis()));
        captchaCodes.setSecretCode(secretCode.toString());
        System.out.println(secretCode.toString());
        captchaCodesRepository.save(captchaCodes);
        return new CaptchaResponse(secretCode.toString(), captchaBaseCode.toString());
    }

    public String generateNewCaptcha() {
        secretCode = new StringBuilder();
        captchaBaseCode = new StringBuilder();
        captchaBaseCode.append(captchaBaseCodePrefix);
        StringBuilder bufferCaptcha = new StringBuilder();
        Random random = new Random();
        int codeLength = 15 + (int) (Math.random() * 10);
        for (int i = 0; i < codeLength; i++) {
            int index = (int) (random.nextFloat() * captchaCodeSymbols.length());
            secretCode.append(captchaCodeSymbols.charAt(index));
        }
        int captchaLength = 4 + (int) (Math.random() * 2);
        while (bufferCaptcha.length() < captchaLength) {
            int index = (int) (random.nextFloat() * captchaSymbols.length());
            bufferCaptcha.append(captchaSymbols.charAt(index));
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
}
