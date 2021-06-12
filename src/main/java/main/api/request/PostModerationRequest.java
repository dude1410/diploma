package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import main.config.Config;

import javax.validation.constraints.NotBlank;

public class PostModerationRequest {

    @JsonProperty("post_id")
    @Schema(description = "идентификатор поста", example = "131")
    private int postId;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Schema(description = "решение по посту: accept или decline", example = "accept")
    private String decision;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
