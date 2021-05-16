package main.controller;

import main.api.response.AuthResponse;
import main.api.response.InitResponse;
import main.api.response.RegisterSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController
{
//	private final InitResponse initResponse;
//	private final AuthResponse authResponse;
//	private final RegisterSuccessResponse successResponse;
//
//	public DefaultController(InitResponse initResponse,
//							 AuthResponse authResponse,
//							 RegisterSuccessResponse successResponse) {
//		this.initResponse = initResponse;
//		this.authResponse = authResponse;
//		this.successResponse = successResponse;
//	}

//	@RequestMapping("/")
//	public String index () {
//		return "index.html";
//	}

//	@GetMapping("/")
//	public String main(Model model) {
//		model.addAttribute("title", "Main page");
//		return "index";
//	}

	@RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET},
			value =  "/**/{path:[^\\.]*}")
	public String index() {
		return "forward:/";
	}
}
