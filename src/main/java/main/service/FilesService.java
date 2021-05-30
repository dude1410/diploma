//package main.service;
//
//import main.api.response.FailResponse;
//import org.apache.tomcat.websocket.Transformation;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import com.cloudinary.utils.ObjectUtils;
//import com.cloudinary.Cloudinary;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.Objects;
//import java.util.Random;
//import java.util.regex.Pattern;
//
//import static main.service.PostService.checkAuthorized;
//
//@Service
//public class FilesService {
//
//    final Pattern IMAGE_NAME = Pattern.compile("^(.*)(.)(png|jpe?g)$"); // какие еще форматы?
//    private final long MAX_IMAGE_SIZE = 5242880;
//    private final String IMAGE_NAME_PREFIX = "/upload/";
//    private final String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//    private final int GENERATE_LENGTH = 6;
//
//    @Value("${blog.cloud_name}")
//    private String CLOUD_NAME;
//    @Value("${blog.api_key}")
//    private String API_KEY;
//    @Value("${blog.api_secret}")
//    private String API_SECRET;
//
//    public Object image(HttpServletRequest request, MultipartFile image){
//
//        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        checkAuthorized(findEmail); // вынес в статик
//
//        if(image.isEmpty()) {
//            createErrorResponse();
//        }
//        if (IMAGE_NAME.matcher(Objects.requireNonNull(image.getOriginalFilename())).matches()){ // непонятно
//            createErrorResponse();
//        }
//        if (image.getSize() > MAX_IMAGE_SIZE) {
//            createErrorResponse();
//        }
//    }
//
//    private FailResponse createErrorResponse(){
//        FailResponse response = new FailResponse();
//        HashMap<String, String> errors = new HashMap<>();
//        errors.put("errors", "Файл должен быть изображением png, jpg, jpeg"); // разные ошибки?
//        response.setResult(false);
//        response.setErrors(errors);
//    }
//
//    private void not() {
//        var cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", CLOUD_NAME,
//                "api_key", API_KEY,
//                "api_secret", API_SECRET));
//
//        Random random = new Random();
//        StringBuilder imageNameBuilder = new StringBuilder();
//        imageNameBuilder.append(IMAGE_NAME_PREFIX);
//        for (int i = 0; i < GENERATE_LENGTH; i++) {
//            int index = (int) (random.nextFloat() * symbols.length());
//            imageNameBuilder.append(symbols.charAt(index));
//        }
//
//
//    }
//}
//
