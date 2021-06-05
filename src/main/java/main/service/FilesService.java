package main.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import main.api.response.FailResponse;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

import static main.service.PostService.checkAuthorized;

@Service
public class FilesService {

    private final Logger logger = LogManager.getLogger(FilesService.class);

    private final Pattern IMAGE_NAME = Pattern.compile("^(.*)(.)(png|jpe?g)$");
    private final long MAX_IMAGE_SIZE = 5242880;
    private final String IMAGE_NAME_PREFIX = "/upload/";
    private final String SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private final String INCORRECT_IMAGE_FORMAT = "Файл должен быть изображением png, jpg, jpeg";
    private final String IMAGE_SIZE_EXCEEDED = "Размер файла должен быть не более 5Мб";
    private final String IMAGE_IS_EMPTY = "Загрузите изображение";


    @Value("${blog.cloud_name}")
    private String CLOUD_NAME;
    @Value("${blog.api_key}")
    private String API_KEY;
    @Value("${blog.api_secret}")
    private String API_SECRET;

    public Object image(HttpServletRequest request, MultipartFile image) throws IOException {

        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);

        if (image.isEmpty()) {
            return createErrorResponse(IMAGE_IS_EMPTY);
        }
        if (!IMAGE_NAME.matcher(Objects.requireNonNull(image.getOriginalFilename())).matches()) {
            return createErrorResponse(INCORRECT_IMAGE_FORMAT);
        }
        if (image.getSize() > MAX_IMAGE_SIZE) {
            return createErrorResponse(IMAGE_SIZE_EXCEEDED);
        }

        String path = IMAGE_NAME_PREFIX + createRandomPath() + image.getOriginalFilename();
        String realPath = request.getServletContext().getRealPath(path);
        System.out.println(realPath);

        byte[] avatar = image.getBytes();
        File file = new File(realPath);
        FileUtils.writeByteArrayToFile(file, avatar);

        return path;
    }

    private FailResponse createErrorResponse(String errorText) {
        FailResponse response = new FailResponse();
        HashMap<String, String> errors = new HashMap<>();
        errors.put("errors", errorText);
        response.setResult(false);
        response.setErrors(errors);
        return response;
    }

    public String cloudStore(BufferedImage photo, String name) throws IOException {

        var cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
        String path = "upload/" + name.substring(0, name.lastIndexOf('.'));

        var params = ObjectUtils.asMap(
                "public_id", path,
                "overwrite", true);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(photo, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var imageString = "data:image/png;base64," +
                DatatypeConverter.printBase64Binary(baos.toByteArray());
        var upload = cloudinary.uploader().upload(imageString, params);

        return upload.get("url").toString();
    }

    private String createRandomPath (){
        StringBuilder randomPath = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            randomPath.append(SYMBOLS.charAt((int) (random.nextFloat() * SYMBOLS.length())));
            randomPath.append(SYMBOLS.charAt((int) (random.nextFloat() * SYMBOLS.length())));
            randomPath.append('/');
        }
        return randomPath.toString();
    }

}

