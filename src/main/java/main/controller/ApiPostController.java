package main.controller;

import main.api.response.PostResponse;
import main.api.response.PostResponseId;
import main.service.PostIdService;
import main.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;
    private final PostIdService postIdService;

    public ApiPostController(PostService postService,
                             PostIdService postIdService) {
        this.postService = postService;
        this.postIdService = postIdService;
    }

    @GetMapping
    private PostResponse postResponse(){
	    return postService.getPostResponse();
    }

//    @GetMapping
//    private PostResponse postResponse(){
//        return postService.getPostEmptyResponse();
//    }

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

//    @GetMapping("/byDate")
//    private PostResponse postResponseByDate(){
//        return postService.getPostEmptyResponse();
//    }

    @GetMapping("/byTag")
    private PostResponse postResponseByTag(){
        return postService.getPostResponse();
    }

//    @GetMapping("/byTag")
//    private PostResponse postResponseByTag(){
//        return postService.getPostEmptyResponse();
//    }

    @GetMapping("/{ID}")
    private ResponseEntity<PostResponseId> postResponseId(@PathVariable String ID){
        if (ID.equals("0")){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(postIdService.getPostResponseId (), HttpStatus.OK);
        }
    }
}
