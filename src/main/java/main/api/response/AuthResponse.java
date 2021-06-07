package main.api.response;

import main.model.DTO.UserTDO;
import org.springframework.stereotype.Component;

@Component
public class AuthResponse {

	private boolean result = true;
	private UserTDO user = new UserTDO();

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public UserTDO getUserTest() {
		return user;
	}

	public void setUserTest(UserTDO user) {
		this.user = user;
	}
}
