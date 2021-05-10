package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.SettingsService;
import main.service.TagTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

	private final SettingsService settingsService;
	private final InitResponse initResponse;
	private final TagTestService tagTestService;

	public ApiGeneralController(SettingsService settingsService,
								InitResponse initResponse, TagResponse tagResponse, TagTestService tagTestService) {
		this.settingsService = settingsService;
		this.initResponse = initResponse;
		this.tagTestService = tagTestService;
	}

	/*
		настройки могут меняться, поэтому мы их будем брать из БД
		нельзя их сделать final как для футера и хэдера
	*/
	@GetMapping("/settings")
	private SettingsResponse settings(){
		return settingsService.getGlobalSettings();
	}

	// инфа для футера и хэдера будет у нас величиной постоянной
	@GetMapping("/init")
	private InitResponse initResponse(){
		return initResponse;
	}

	@GetMapping("/tag")
	private TagResponse tagResponse() {
		return TagTestService.getTagResponse();
	}
}
