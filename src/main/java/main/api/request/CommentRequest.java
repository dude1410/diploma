package main.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import main.config.Config;

import javax.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {

    @JsonProperty("parent_id")
    @Schema(description = "ID комментария, на который пишется ответ", example = "31")
    private Integer parentId;

    @JsonProperty("post_id")
    @Schema(description = "ID поста, к которому пишется ответ", example = "21")
    private int postId;

    @JsonProperty("text")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Schema(description = "текст комментария (формат HTML)", example = "текст комментария")
    private String text;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
