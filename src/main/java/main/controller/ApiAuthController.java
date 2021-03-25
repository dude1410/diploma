package main.controller;

import main.api.response.AuthResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {

	private final AuthResponse authResponse;

	public ApiAuthController(AuthResponse authResponse) {
		this.authResponse = authResponse;
	}

	@RequestMapping("/api/auth/")
	private AuthResponse authResponse(){
		return authResponse;
	}
}
