package main.controller;

import main.api.response.AuthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

	private final AuthResponse authResponse;

	public ApiAuthController(AuthResponse authResponse){
		this.authResponse = authResponse;
	}

	@GetMapping("/check")
	private AuthResponse authResponse() {
		return authResponse;
	}
}
