package main.controller;

import main.api.request.ChangePasswordRequest;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestorePasswordRequest;
import main.api.response.*;
import main.repository.UserRepository;
import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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

	@GetMapping("/check")
	public ResponseEntity<LoginResponse> check(Principal principal) throws IOException {
		if (principal == null) {
			return ResponseEntity.ok(new LoginResponse());
		}
		return authService.check();
	}

	@GetMapping("/captcha")
	private CaptchaResponse captchaResponse() throws IOException {
		return authService.getCaptcha();
	}

	@PostMapping("/register")
	private FailResponse register(@RequestBody RegisterRequest request) throws IOException {
		return authService.register(request);
	}

	@PostMapping("/restore")
	private FailResponse restorePassword (@RequestBody RestorePasswordRequest request) throws MessagingException {
		return authService.restorePassword(request);
	}

	@PostMapping("/password")
	private FailResponse changePassword (@RequestBody ChangePasswordRequest request) {
		return authService.changePassword(request);
	}
}
