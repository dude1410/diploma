package main.controller;

import main.api.request.CommentRequest;
import main.api.request.PostModerationRequest;
import main.api.response.*;
import main.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

	private final SettingsService settingsService;
	private final InitResponse initResponse;
	private final TagsService tagsService;
	private final PostService postService;
//	private final FilesService filesService;
	private final CommentService commentService;

	public ApiGeneralController(SettingsService settingsService,
								InitResponse initResponse,
								TagsService tagsService,
								PostService postService,
//								FilesService filesService,
								CommentService commentService) {
		this.settingsService = settingsService;
		this.initResponse = initResponse;
		this.tagsService = tagsService;
		this.postService = postService;
//		this.filesService = filesService;
		this.commentService = commentService;
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

//	// todo: finish the method
//	@PostMapping(value = "/api/image",
//			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	private ResponseEntity<?> postImage (HttpServletRequest servlet,
//										 @RequestParam("image") MultipartFile image) {
//		return ResponseEntity.ok(filesService.image(servlet, image));
//	}

	@PostMapping("/comment")
	private FailResponse postComment(@RequestBody CommentRequest request) {
		return commentService.postComment(request);
	}

	@PostMapping("/moderation")
	private FailResponse postModeration (@RequestBody PostModerationRequest request){
		return postService.postModeration(request);
	}
}
