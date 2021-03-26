package main.controller;

import main.api.response.AuthResponse;
import main.service.AuthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {

	private final AuthService authService;

	public ApiAuthController(AuthService authService) {
		this.authService = authService;
	}

	@RequestMapping("/api/auth/")
	private AuthResponse response(){
		return authService.getAuthResponse();
	}
}
