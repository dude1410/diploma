package main.service;

import main.api.response.StatisticsResponse;
import main.model.Post;
import main.model.User;
import main.repository.GlobalSettingsRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static main.service.PostService.checkAuthorized;

@Service
public class StatisticsService {

    private final Logger logger = LogManager.getLogger(StatisticsService.class);

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final GlobalSettingsRepository globalSettingsRepository;

    @Autowired
    public StatisticsService(UserRepository userRepository,
                             PostRepository postRepository,
                             GlobalSettingsRepository globalSettingsRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.globalSettingsRepository = globalSettingsRepository;
    }

    public StatisticsResponse statisticsMy() {

        logger.info("Начало авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Авторизация прошла успешно");
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            logger.error("Ошибка! Пользователь с почтовым ящиком " + findEmail + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List <Post> allMyPosts = postRepository.findAllMyPostsByEmail(findEmail);
        if (allMyPosts.isEmpty()) {
            logger.error("Ошибка - посты по пользователю " + userInDB.getName() + " не найдены");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return createStatisticsResponse(allMyPosts);
    }

    public StatisticsResponse statisticsAll () {
        logger.info("Начало авторизации");
        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        logger.info("Авторизация прошла успешно");
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            logger.error("Ошибка! Пользователь с почтовым ящиком " + findEmail + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String statistics = globalSettingsRepository.findStatisticsIsPublic();
        if (userInDB.isIs_moderator() == 1 || statistics.equals("YES")) {
            List<Post> allPosts = postRepository.allPosts();
            return createStatisticsResponse(allPosts);
        } else {
            logger.error("Ошибка - пользователь не явлется модератором или отключен многопользовательский режим");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private StatisticsResponse createStatisticsResponse (List<Post> posts) {
        logger.info("Собирается статистика по " + posts.size() + " постам");
        StatisticsResponse response = new StatisticsResponse();
        response.setPostsCount(posts.size());
        response.setFirstPublication(posts.get(0).getTime().getTime() / 1000);
        int likeCount = 0;
        int dislikeCount = 0;
        int viewCount = 0;
        for (Post post : posts) {
            likeCount = likeCount + ((int) post.getPostVotes().stream().filter(a -> a.getValue() == 1).count());
            dislikeCount = dislikeCount + ((int) post.getPostVotes().stream().filter(a -> a.getValue() == 0).count());
            viewCount = viewCount + post.getViewCount();
        }
        response.setLikesCount(likeCount);
        response.setDislikesCount(dislikeCount);
        response.setViewsCount(viewCount);
        return response;
    }
}
