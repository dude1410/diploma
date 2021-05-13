package main.controller;

import main.api.response.PostResponse;
import main.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/post")
    private PostResponse postResponse(){
	    return postService.getPostResponse();
    }
}
