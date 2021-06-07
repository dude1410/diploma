package main.config;

public class Config {
    public static final String STRING_FIELD_CANT_BE_BLANK = "Поле не может быть пустым.";
    public static final String STRING_FIELD_POST_ID = "Номер поста не может быть пустым.";
    public static final String STRING_AUTH_INVALID_EMAIL = "Адрес почты указан неверно.";
    public static final String STRING_AUTH_PASSWORD = "Длина пароля от 6 до 255 символов.";
    public static final String STRING_AUTH_NAME = "Длина имени от 3 до 20 символов.";
    public static final String STRING_AUTH_ERROR = "Ошибка аутентификации";
    public static final String STRING_NEW_POST_TITLE = "Длина названия поста от 3 до 255 символов";
    public static final String STRING_NEW_POST_TEXT = "Длина названия поста от 3 до 255 символов";
    public static final String STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD = "Адрес или пароль не указаны.";
    public static final String STRING_AUTH_LOGIN_NO_SUCH_USER = "Пользователь не найден.";
    public static final String STRING_AUTH_WRONG_PASSWORD = "Пароль указан неверно.";
    public static final String STRING_AUTH_WRONG_FORMAT = "Неверный формат логина или пароля";
    public static final String CAPTCHA_BASE_CODE_PREFIX = "data:image/png;base64, ";
    public static final String CAPTCHA_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    public static final String CAPTCHA_CODE_SYMBOLS = "abcdefghijklmnopqrstuvwxyz1234567890";
    public static final String STRING_COMMENT_IS_EMPTY_OR_SHORT = "Текст комментария не задан или слишком короткий";

    public static final int INT_AUTH_BCRYPT_STRENGTH = 12;
    public static final int INT_CAPTCHA_LENGTH = 3;
    public static final int HASH_LENGTH = 45;
    public static final int DELETE_CAPTCHA_TIME_PERIOD = 3600;

    public static final long CODE_LIFETIME = 86400000L; // 24 h


}
