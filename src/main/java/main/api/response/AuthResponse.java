package main.api.response;

import main.testEntity.UserTest;
import org.springframework.stereotype.Component;

@Component
public class AuthResponse {

	private boolean result = true;
	private UserTest user = new UserTest();

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public UserTest getUserTest() {
		return user;
	}

	public void setUserTest(UserTest user) {
		this.user = user;
	}
}
