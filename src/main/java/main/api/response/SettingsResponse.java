package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsResponse {

	@JsonProperty("MULTIUSER_MODE")
	private String multiuserMode;

	@JsonProperty("POST_PREMODERATION")
	private String postPremoderation;

	@JsonProperty("STATISTICS_IS_PUBLIC")
	private String statisticsIsPublic;

	public String getMultiuserMode() {
		return multiuserMode;
	}

	public void setMultiuserMode(String multiuserMode) {
		this.multiuserMode = multiuserMode;
	}

	public String getPostPremoderation() {
		return postPremoderation;
	}

	public void setPostPremoderation(String postPremoderation) {
		this.postPremoderation = postPremoderation;
	}

	public String getStatisticsIsPublic() {
		return statisticsIsPublic;
	}

	public void setStatisticsIsPublic(String statisticsIsPublic) {
		this.statisticsIsPublic = statisticsIsPublic;
	}
}
