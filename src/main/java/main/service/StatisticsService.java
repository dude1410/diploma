package main.service;

import main.api.response.StatisticsResponse;
import main.model.Post;
import main.model.User;
import main.repository.GlobalSettingsRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static main.service.PostService.checkAuthorized;

@Service
public class StatisticsService {

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

        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List <Post> allMyPosts = postRepository.findAllMyPostsByEmail(findEmail);
        if (allMyPosts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return createStatisticsResponse(allMyPosts);
    }

    public StatisticsResponse statisticsAll () {

        String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        checkAuthorized(findEmail);
        User userInDB = userRepository.findByEmail(findEmail);
        if (userInDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String statistics = globalSettingsRepository.findStatisticsIsPublic();
        if (userInDB.isIs_moderator() == 1 || statistics.equals("YES")) {
            List<Post> allPosts = postRepository.allPosts();
            return createStatisticsResponse(allPosts);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private StatisticsResponse createStatisticsResponse (List<Post> posts) {
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
