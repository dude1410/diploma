package main.api.response;

import main.model.UserTest;
import org.springframework.stereotype.Component;

@Component
public class AuthResponse {

	private boolean result = true;
//	private boolean result = false;
	private UserTest userTest = new UserTest();

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public UserTest getUserTest() {
		return userTest;
	}

	public void setUserTest(UserTest userTest) {
		this.userTest = userTest;
	}
}
