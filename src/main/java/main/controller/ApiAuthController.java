package main.controller;

import main.api.response.AuthResponse;
import main.api.response.CaptchaResponse;
import main.api.response.RegisterFailResponse;
import main.api.response.RegisterSuccessResponse;
import main.service.CaptchaService;
import main.service.RegisterFailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

	private final AuthResponse authResponse;
	private final CaptchaService captchaService;
	private final RegisterSuccessResponse successResponse;
	private final RegisterFailService failService;

	public ApiAuthController(AuthResponse authResponse, CaptchaService captchaService, RegisterSuccessResponse successResponse, RegisterFailService failService){
		this.authResponse = authResponse;
		this.captchaService = captchaService;
		this.successResponse = successResponse;
		this.failService = failService;
	}

	@GetMapping("/check")
	private AuthResponse authResponse() {
		return authResponse;
	}

	@GetMapping("/captcha")
	private CaptchaResponse captchaResponse(){
		return  captchaService.getCaptcha ();
	}

	@PostMapping("/register")
	private RegisterFailResponse failResponse(){
		return RegisterFailService.getResponse();
	}
}
