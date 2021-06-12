package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class SettingsRequest {

    @JsonProperty("MULTIUSER_MODE")
    @Schema(description = "многопользовательский режим", example = "true")
    private boolean multiuserMode;

    @JsonProperty("POST_PREMODERATION")
    @Schema(description = "режим премодерации", example = "false")
    private boolean postPremoderation;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    @Schema(description = "рижим доступа к статистике блога", example = "true")
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
