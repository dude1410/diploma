package main.controller;

import main.api.response.PostResponse;
import main.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    private PostResponse postResponse(){
	    return postService.getPostResponse();
    }

//    @GetMapping("/search")
//    private PostResponse postResponseSearch(){
//        return postService.getPostEmptyResponse();
//    }

    @GetMapping("/search")
    private PostResponse postResponseSearch(){
        return postService.getPostResponse();
    }

    @GetMapping("/byDate")
    private PostResponse postResponseByDate(){
        return postService.getPostResponse();
    }
}
