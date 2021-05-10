package main.controller;

import main.api.response.AuthResponse;
import main.api.response.InitResponse;
import main.api.response.PostResponse;
import main.service.TagTestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController
{
	private final InitResponse initResponse;
	private final AuthResponse authResponse;
	private final PostResponse postResponse;
	private final TagTestService tagTestService;

	public DefaultController(InitResponse initResponse,
							 AuthResponse authResponse, PostResponse postResponse,
							 TagTestService tagTestService) {
		this.initResponse = initResponse;
		this.authResponse = authResponse;
		this.postResponse = postResponse;
		this.tagTestService = tagTestService;
	}

//	@RequestMapping("/")
//	public String index () {
//		return "index.html";
//	}

	@RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET},
			value =  "/**/{path:[^\\.]*}")
	public String index() {
		return "forward:/";
	}
}
