package main.controller;

import main.api.response.*;
import main.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

	private final SettingsService settingsService;
	private final InitResponse initResponse;
	private final TagsService tagsService;
	private final PostService postService;

	public ApiGeneralController(SettingsService settingsService,
								InitResponse initResponse,
								TagsService tagsService,
								PostService postService) {
		this.settingsService = settingsService;
		this.initResponse = initResponse;
		this.tagsService = tagsService;
		this.postService = postService;
	}

	@GetMapping("/settings")
	private SettingsResponse settings(){
		return settingsService.getGlobalSettings();
	}

	@GetMapping("/init")
	private InitResponse initResponse(){
		return initResponse;
	}

	@GetMapping("/tag")
	private TagResponse tagResponse(@RequestParam(required = false)String query) {
		return tagsService.getTagResponse(query);
	}

	@GetMapping("/calendar")
	private CalendarResponse calendarResponse (){
		return postService.getCalendar();
	}
}
