package main.controller;

import main.api.response.PostResponse;
import main.api.response.PostResponseId;
import main.service.PostIdService;
import main.service.PostService;
import org.springframework.web.bind.annotation.*;

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
    private PostResponse getAllPostResponse(@RequestParam("limit") int limit,
                                            @RequestParam("offset") int offset,
                                            @RequestParam("mode") String mode){
	    return postService.getAllPostResponse(limit, offset, mode);
    }

    @GetMapping("/search")
    private PostResponse getSearchPostResponse(@RequestParam("limit") int limit,
                                            @RequestParam("offset") int offset,
                                            @RequestParam("query") String query){
        return postService.getSearchPostResponse(limit, offset, query);
    }

    @GetMapping("/byDate")
    private PostResponse getSearchByDatePostResponse(@RequestParam("limit") int limit,
                                               @RequestParam("offset") int offset,
                                               @RequestParam("date") String date){
        return postService.getSearchByDatePostResponse(limit, offset, date);
    }

    @GetMapping("/byTag")
    private PostResponse getSearchByTagPostResponse(@RequestParam("limit") int limit,
                                                     @RequestParam("offset") int offset,
                                                     @RequestParam("tag") String tag){
        return postService.getSearchByTagPostResponse(limit, offset, tag);
    }

    //todo: исправить авторизацию
    @GetMapping("/{id}")
    private PostResponseId getPostById(@PathVariable int id){
        return postService.getPostById(id);
    }
}
