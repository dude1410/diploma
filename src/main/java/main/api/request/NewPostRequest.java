package main.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import main.config.Config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

public class NewPostRequest {

    @Schema(description = "дата и время публикации в формате UTC", example = "1592338706")
    private Date timestamp;

    @Schema(description = "1 или 0, открыт пост или скрыт", example = "1")
    private int active;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 3, max = 255, message = Config.STRING_NEW_POST_TITLE)
    @Schema(description = "заголовок поста", example = "заголовок")
    private String title;

    @Schema(description = "тэги через запятую (при вводе на frontend тэг добавляется при нажатии Enter)", example = "java, spring")
    private Set<String> tags;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 50, message = Config.STRING_NEW_POST_TEXT)
    @Schema(description = "текст поста в формате HTML", example = "Текст поста включащий <b>тэги форматирования</b>")
    private String text;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
