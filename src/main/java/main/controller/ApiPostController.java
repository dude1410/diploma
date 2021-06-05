package main.controller;

import main.api.request.LikeDislikeRequest;
import main.api.request.NewPostRequest;
import main.api.response.FailResponse;
import main.api.response.PostResponse;
import main.api.response.PostResponseId;
import main.service.PostService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
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

    @GetMapping("/{id}")
    private PostResponseId getPostById(@PathVariable int id){
        return postService.getPostById(id);
    }

    @GetMapping("/moderation")
    private PostResponse getPostModeration(@RequestParam("limit") int limit,
                                           @RequestParam("offset") int offset,
                                           @RequestParam("status") String status){
        return postService.getPostModeration(limit, offset, status);
    }

    @GetMapping("/my")
    private PostResponse getMyPostsResponse (@RequestParam("limit") int limit,
                                             @RequestParam("offset") int offset,
                                             @RequestParam("status") String status) {
        return postService.getMyPostsResponse(limit, offset, status);
    }

    @PostMapping
    private FailResponse postNewPost (@Valid @RequestBody NewPostRequest request,
                                      BindingResult error) {
        return postService.postNewPost(request, error);
    }

    @PutMapping("/{id}")
    private FailResponse putPost (@PathVariable int id, @Valid @RequestBody NewPostRequest request,
                                  BindingResult error){
        return postService.putPost(id, request, error);
    }

    @PostMapping("/like")
    private FailResponse postLike (@Valid @RequestBody LikeDislikeRequest request,
                                   BindingResult error) {
        return postService.postLike(request, error);
    }

    @PostMapping("/dislike")
    private FailResponse postDislike (@Valid @RequestBody LikeDislikeRequest request,
                                      BindingResult error) {
        return postService.postDislike(request, error);
    }
}
