package main.controller;

import main.api.request.CommentRequest;
import main.api.request.MyProfileRequest;
import main.api.request.PostModerationRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

	private final SettingsService settingsService;
	private final InitResponse initResponse;
	private final TagsService tagsService;
	private final PostService postService;
	private final FilesService filesService;
	private final CommentService commentService;
	private final StatisticsService statisticsService;
	private final MyProfileService myProfileService;

	public ApiGeneralController(SettingsService settingsService,
								InitResponse initResponse,
								TagsService tagsService,
								PostService postService,
								FilesService filesService,
								CommentService commentService,
								StatisticsService statisticsService,
								MyProfileService myProfileService) {
		this.settingsService = settingsService;
		this.initResponse = initResponse;
		this.tagsService = tagsService;
		this.postService = postService;
		this.filesService = filesService;
		this.commentService = commentService;
		this.statisticsService = statisticsService;
		this.myProfileService = myProfileService;
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

	@PostMapping(value = "/image",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity postImage (HttpServletRequest servlet,
										 @RequestParam("image") MultipartFile image) throws IOException {
		return ResponseEntity.ok(filesService.image(servlet, image));
	}

	@PostMapping("/comment")
	private FailResponse postComment(@RequestBody CommentRequest request) {
		return commentService.postComment(request);
	}

	@PostMapping("/moderation")
	private FailResponse postModeration (@RequestBody PostModerationRequest request){
		return postService.postModeration(request);
	}

	@GetMapping("/statistics/my")
	private StatisticsResponse statisticsMy() {
		return statisticsService.statisticsMy();
	}

	@GetMapping("/statistics/all")
	private StatisticsResponse statisticsAll() {
		return statisticsService.statisticsAll();
	}

	@PutMapping("/settings")
	private void putSettings(@RequestBody SettingsRequest request){
		settingsService.putSettings(request);
	}

	@PostMapping(value = "/profile/my")
	private FailResponse postMyProfileNewPhoto (@ModelAttribute MyProfileRequest request) throws IOException {
		return myProfileService.postMyProfileNewPhoto(request);
	}
}
