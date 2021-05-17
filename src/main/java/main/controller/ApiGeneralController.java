package main.controller;

import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.CalendarService;
import main.service.SettingsService;
import main.service.TagTestService;
import main.service.TagsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

	private final SettingsService settingsService;
	private final InitResponse initResponse;
//	private final TagTestService tagTestService;
	private final CalendarService calendarService;
	private final TagsService tagsService;

	public ApiGeneralController(SettingsService settingsService,
								InitResponse initResponse,
//								TagTestService tagTestService,
								CalendarService calendarService,
								TagsService tagsService) {
		this.settingsService = settingsService;
		this.initResponse = initResponse;
//		this.tagTestService = tagTestService;
		this.calendarService = calendarService;
		this.tagsService = tagsService;
	}

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
	private TagResponse tagResponse(@RequestParam(required = false)String query) {
		return tagsService.getTagResponse(query);
	}

	@GetMapping("/calendar")
	private CalendarResponse calendarService(){
		return calendarService.getCalendarResponse();
	}
}
