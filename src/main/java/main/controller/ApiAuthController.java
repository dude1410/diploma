package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import main.api.request.ChangePasswordRequest;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestorePasswordRequest;
import main.api.response.*;
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
@Tag(name = "/api/auth", description = "Операции с профилем")
public class ApiAuthController {

	private final AuthService authService;

	@Autowired
	public ApiAuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping(value = "/login",
			consumes = "application/json",
			produces = "application/json")
	@Operation(description = "Вход в учетную запись")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
											   BindingResult error) {
		return authService.login(loginRequest, error);
	}

	@GetMapping(value = "/logout",
			produces = "application/json")
	@Operation(description = "Выход из учетной записи")
	public ResponseEntity<LogoutResponse> logout() {
		return authService.logout();
	}

	@GetMapping(value = "/check",
			produces = "application/json")
	@Operation(description = "Проверка авторизации")
	public ResponseEntity<LoginResponse> check(Principal principal) throws IOException {
		if (principal == null) {
			return ResponseEntity.ok(new LoginResponse());
		}
		return authService.check();
	}

	@GetMapping(value = "/captcha",
			produces = "application/json")
	@Operation(description = "Генерация капчи")
	private CaptchaResponse captchaResponse() throws IOException {
		return authService.getCaptcha();
	}

	@PostMapping(value = "/register",
			consumes = "application/json",
			produces = "application/json")
	@Operation(description = "Регистрация на сайте")
	private FailResponse register(@Valid @RequestBody RegisterRequest request,
								  BindingResult error) throws IOException {
		return authService.register(request, error);
	}

	@PostMapping(value = "/restore",
			consumes = "application/json",
			produces = "application/json")
	@Operation(description = "Ссылка на восстановление пароля")
	private FailResponse restorePassword (@Valid @RequestBody RestorePasswordRequest request,
										  BindingResult error) throws MessagingException {
		return authService.restorePassword(request, error);
	}

	@PostMapping(value = "/password",
			consumes = "application/json",
			produces = "application/json")
	@Operation(description = "Восстановление пароля")
	private FailResponse changePassword (@Valid @RequestBody ChangePasswordRequest request,
										 BindingResult error) {
		return authService.changePassword(request,error);
	}
}
