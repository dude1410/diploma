package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.config.Config;

import javax.validation.constraints.NotBlank;

public class SettingsRequest {

    @JsonProperty("MULTIUSER_MODE")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private boolean multiuserMode;

    @JsonProperty("POST_PREMODERATION")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private boolean postPremoderation;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private boolean statisticsIsPublic;

    public boolean isMultiuserMode() {
        return multiuserMode;
    }

    public void setMultiuserMode(boolean multiuserMode) {
        this.multiuserMode = multiuserMode;
    }

    public boolean isPostPremoderation() {
        return postPremoderation;
    }

    public void setPostPremoderation(boolean postPremoderation) {
        this.postPremoderation = postPremoderation;
    }

    public boolean isStatisticsIsPublic() {
        return statisticsIsPublic;
    }

    public void setStatisticsIsPublic(boolean statisticsIsPublic) {
        this.statisticsIsPublic = statisticsIsPublic;
    }
}
