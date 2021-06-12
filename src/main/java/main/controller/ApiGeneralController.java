package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import main.api.request.*;
import main.api.response.*;
import main.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Tag(name = "/api", description = "Общие операции через api")
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

    @GetMapping(value = "/settings",
            produces = "application/json")
    @Operation(description = "Получение глобальных настроек блога")
    private SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping(value = "/init",
            produces = "application/json")
    @Operation(description = "Получение футера")
    private InitResponse initResponse() {
        return initResponse;
    }

    @GetMapping(value = "/tag",
            produces = "application/json")
    @Operation(description = "Получение списка тэгов")
    private TagResponse tagResponse(@RequestParam(required = false) String query) {
        return tagsService.getTagResponse(query);
    }

    @GetMapping(value = "/calendar",
            produces = "application/json")
    @Operation(description = "Показать посты в календаре")
    private CalendarResponse calendarResponse() {
        return postService.getCalendar();
    }

    @PostMapping(value = "/image",
            consumes = "multipart/form-data",
            produces = "application/json")
    @Operation(description = "Загрузка изображений")
    private ResponseEntity postImage(HttpServletRequest servlet,
                                     @RequestParam("image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(filesService.image(servlet, image));
    }

    @PostMapping(value = "/comment",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Опубликовать комментарий")
    private FailResponse postComment(@Valid @RequestBody CommentRequest request,
                                     BindingResult error) {
        return commentService.postComment(request, error);
    }

    @PostMapping(value = "/moderation",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Модерация постов")
    private FailResponse postModeration(@Valid @RequestBody PostModerationRequest request,
                                        BindingResult error) {
        return postService.postModeration(request, error);
    }

    @GetMapping(value = "/statistics/my",
            produces = "application/json")
    @Operation(description = "Получить статистику по своим постам")
    private StatisticsResponse statisticsMy() {
        return statisticsService.statisticsMy();
    }

    @GetMapping(value = "/statistics/all",
            produces = "application/json")
    @Operation(description = "Получить общую статистику по блогу")
    private StatisticsResponse statisticsAll() {
        return statisticsService.statisticsAll();
    }

    @PutMapping(value = "/settings",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Изменение настроек блога")
    private void putSettings(@Valid @RequestBody SettingsRequest request,
                             BindingResult error) {
        settingsService.putSettings(request, error);
    }

    @PostMapping(value = "/profile/my",
            consumes = "multipart/form-data",
            produces = "application/json")
    @Operation(description = "Редактирование профиля с загрузкой фото")
    private FailResponse postMyProfileNewPhoto(@ModelAttribute MyProfileRequest request,
                                               BindingResult error) throws IOException {
        return myProfileService.postMyProfileNewPhoto(request, error);
    }

    @PostMapping(value = "/profile/my",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Редактирование профиля без загрузки фото")
    private FailResponse postMyProfileNoPhoto(@Valid @RequestBody MyProfileRequestNoPhoto request,
                                              BindingResult error) throws IOException {
        return myProfileService.postMyProfileNoPhoto(request, error);
    }
}
