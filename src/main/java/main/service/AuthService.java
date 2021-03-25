package main.service;

import main.api.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	public AuthResponse getAuthResponse(){
		AuthResponse authResponse = new AuthResponse();
		authResponse.setResult(false);
		return authResponse;
	}
}
