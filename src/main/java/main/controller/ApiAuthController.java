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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

	private final UserRepository userRepository;
	private final AuthService authService;

	@Autowired
	public ApiAuthController(UserRepository userRepository,
							 AuthService authService) {
		this.authService = authService;
		this.userRepository = userRepository;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
											   BindingResult error) {
		return authService.login(loginRequest, error);
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
	private FailResponse register(@Valid @RequestBody RegisterRequest request,
								  BindingResult error) throws IOException {
		return authService.register(request, error);
	}

	@PostMapping("/restore")
	private FailResponse restorePassword (@Valid @RequestBody RestorePasswordRequest request,
										  BindingResult error) throws MessagingException {
		return authService.restorePassword(request, error);
	}

	@PostMapping("/password")
	private FailResponse changePassword (@Valid @RequestBody ChangePasswordRequest request,
										 BindingResult error) {
		return authService.changePassword(request,error);
	}
}
