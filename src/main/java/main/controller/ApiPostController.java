package main.controller;

import main.api.response.PostResponse;
import main.api.response.PostResponseId;
import main.service.PostIdService;
import main.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    @GetMapping
//    private PostResponse postResponse(){
//        return postService.getPostEmptyResponse();
//    }
//
//    @GetMapping("/search")
//    private PostResponse postResponseSearch(){
//        return postService.getPostEmptyResponse();
//    }
//
//    @GetMapping("/search")
//    private PostResponse postResponseSearch(){
//        return postService.getPostResponse();
//    }
//
//    @GetMapping("/byDate")
//    private PostResponse postResponseByDate(){
//        return postService.getPostResponse();
//    }
//
//    @GetMapping("/byDate")
//    private PostResponse postResponseByDate(){
//        return postService.getPostEmptyResponse();
//    }
//
//    @GetMapping("/byTag")
//    private PostResponse postResponseByTag(){
//        return postService.getPostResponse();
//    }
//
////    @GetMapping("/byTag")
//    private PostResponse postResponseByTag(){
//        return postService.getPostEmptyResponse();
//    }
//
//    @GetMapping("/{ID}")
//    private ResponseEntity<PostResponseId> postResponseId(@PathVariable String ID){
//        if (ID.equals("0")){
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        } else {
//            return new ResponseEntity<>(postIdService.getPostResponseId (), HttpStatus.OK);
//        }
//    }
}
