package main.controller;

import main.api.request.LoginRequest;
import main.api.response.*;
import main.repository.UserRepository;
import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

	private final UserRepository userRepository;
	private final AuthService authService;

	@Autowired
	public ApiAuthController(UserRepository userRepository, AuthService authService) {
		this.authService = authService;
		this.userRepository = userRepository;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@GetMapping("/logout")
	public ResponseEntity<LogoutResponse> logout() {
		return authService.logout();
	}

//	private final AuthResponse authResponse;
//	private final CaptchaService captchaService;
//	private final RegisterSuccessResponse successResponse;
//	private final RegisterFailService failService;
//
//	public ApiAuthController(AuthResponse authResponse, CaptchaService captchaService, RegisterSuccessResponse successResponse, RegisterFailService failService){
//		this.authResponse = authResponse;
//		this.captchaService = captchaService;
//		this.successResponse = successResponse;
//		this.failService = failService;
//	}
//
	@GetMapping("/check")
	public ResponseEntity<LoginResponse> check(Principal principal) throws IOException {
		if (principal == null) {
			return ResponseEntity.ok(new LoginResponse());
		}
		return authService.check(principal);
	}

	@GetMapping("/captcha")
	private CaptchaResponse captchaResponse() throws IOException {
		return authService.getCaptcha();
	}

//	@PostMapping("/register")
//	private RegisterFailResponse failResponse(){
//		return RegisterFailService.getResponse();
//	}
}
