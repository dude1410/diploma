package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.service.SettingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

	private final SettingsService settingsService;
	private final InitResponse initResponse;

	public ApiGeneralController(SettingsService settingsService, InitResponse initResponse) {
		this.settingsService = settingsService;
		this.initResponse = initResponse;
	}

	/*
		настройки могут меняться, поэтому мы их будем брать из БД
		нельзя их сделать final как для футера и хэдера
		 */
	@GetMapping("/api/settings")
	private SettingsResponse settings(){
		return settingsService.getGlobalSettings();
	}

	// инфа для футера и хэдера будет у нас величиной постоянной
	@GetMapping("/api/init")
	private InitResponse initResponse(){
		return initResponse;
	}


}
