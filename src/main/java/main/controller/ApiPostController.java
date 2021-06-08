package main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import main.api.request.LikeDislikeRequest;
import main.api.request.NewPostRequest;
import main.api.response.FailResponse;
import main.api.response.PostResponse;
import main.api.response.PostResponseId;
import main.service.PostService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/post")
@Api(value = "/api/auth", description = "Операции с постами")
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping(produces = "application/json")
    @ApiOperation(value = "Получение всех постов")
    private PostResponse getAllPostResponse(@RequestParam("limit") int limit,
                                            @RequestParam("offset") int offset,
                                            @RequestParam("mode") String mode){
	    return postService.getAllPostResponse(limit, offset, mode);
    }

    @GetMapping(value = "/search",
            produces = "application/json")
    @ApiOperation(value = "Поиск постов по названию или тексту")
    private PostResponse getSearchPostResponse(@RequestParam("limit") int limit,
                                            @RequestParam("offset") int offset,
                                            @RequestParam("query") String query){
        return postService.getSearchPostResponse(limit, offset, query);
    }

    @GetMapping(value = "/byDate",
            produces = "application/json")
    @ApiOperation(value = "Поиск постов по дате")
    private PostResponse getSearchByDatePostResponse(@RequestParam("limit") int limit,
                                               @RequestParam("offset") int offset,
                                               @RequestParam("date") String date){
        return postService.getSearchByDatePostResponse(limit, offset, date);
    }

    @GetMapping(value = "/byTag",
            produces = "application/json")
    @ApiOperation(value = "Поиск постов по тэгу")
    private PostResponse getSearchByTagPostResponse(@RequestParam("limit") int limit,
                                                     @RequestParam("offset") int offset,
                                                     @RequestParam("tag") String tag){
        return postService.getSearchByTagPostResponse(limit, offset, tag);
    }

    @GetMapping(value = "/{id}",
            produces = "application/json")
    @ApiOperation(value = "Поличть пост из БД по id")
    private PostResponseId getPostById(@PathVariable int id){
        return postService.getPostById(id);
    }

    @GetMapping(value = "/moderation",
            produces = "application/json")
    @ApiOperation(value = "Получение постов для подерации")
    private PostResponse getPostModeration(@RequestParam("limit") int limit,
                                           @RequestParam("offset") int offset,
                                           @RequestParam("status") String status){
        return postService.getPostModeration(limit, offset, status);
    }

    @GetMapping(value = "/my",
            produces = "application/json")
    @ApiOperation(value = "Получение своих постов")
    private PostResponse getMyPostsResponse (@RequestParam("limit") int limit,
                                             @RequestParam("offset") int offset,
                                             @RequestParam("status") String status) {
        return postService.getMyPostsResponse(limit, offset, status);
    }

    @PostMapping(consumes = "multipart/form-data",
            produces = "application/json")
    @ApiOperation(value = "Создание нового поста")
    private FailResponse postNewPost (@Valid @RequestBody NewPostRequest request,
                                      BindingResult error) {
        return postService.postNewPost(request, error);
    }

    @PutMapping(value = "/{id}",
            consumes = "multipart/form-data",
            produces = "application/json")
    @ApiOperation(value = "Редактирование поста по id")
    private FailResponse putPost (@PathVariable int id, @Valid @RequestBody NewPostRequest request,
                                  BindingResult error){
        return postService.putPost(id, request, error);
    }

    @PostMapping(value = "/like",
            consumes = "multipart/form-data",
            produces = "application/json")
    @ApiOperation(value = "Поставить посту лайк")
    private FailResponse postLike (@Valid @RequestBody LikeDislikeRequest request,
                                   BindingResult error) {
        return postService.postLike(request, error);
    }

    @PostMapping(value = "/dislike",
            consumes = "multipart/form-data",
            produces = "application/json")
    @ApiOperation(value = "Поставить посту дизлайк")
    private FailResponse postDislike (@Valid @RequestBody LikeDislikeRequest request,
                                      BindingResult error) {
        return postService.postDislike(request, error);
    }
}
