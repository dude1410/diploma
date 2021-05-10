package main.controller;

import main.api.response.PostResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

    private final PostResponse postResponse;

    public ApiPostController(PostResponse postResponse) {
        this.postResponse = postResponse;
    }

    @GetMapping("/api/post/")
    private PostResponse postResponse(){
	    return postResponse;
    }
}
