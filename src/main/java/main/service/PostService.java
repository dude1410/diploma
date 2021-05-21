package main.service;

import main.api.response.CalendarResponse;
import main.api.response.PostResponse;
import main.model.Post;
import main.repository.PostRepository;
import main.testEntity.PostTest;
import main.testEntity.UserTestForPostTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
//    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository
//                       UserRepository userRepository
    ) {
        this.postRepository = postRepository;
//        this.userRepository = userRepository;
    }

    public PostResponse getAllPostResponse(int limit, int offset, String mode){
        PostResponse allPosts = new PostResponse();
        List<PostTest> posts = new ArrayList<>();
        Page<Post> postsToShow;

        int postCount = 0;

        if (mode == null) {mode = "recent";}
        if (mode.equalsIgnoreCase("recent")) {
            postsToShow = postRepository.findPostsByTimeNewFist(PageRequest.of(offset / limit, limit));
            postCount = (int) postsToShow.getTotalElements();
            for (Post post : postsToShow) {
                PostTest postTest = newPostTest(post);
                if (!posts.contains(postTest)) {
                    posts.add(postTest);
                }
            }
        } else if (mode.equalsIgnoreCase("early")) {
            postsToShow = postRepository.findPostsByTimeOldFist(PageRequest.of(offset / limit, limit));
            postCount = (int) postsToShow.getTotalElements();
            for (Post post : postsToShow) {
                PostTest postTest = newPostTest(post);
                if (!posts.contains(postTest)) {
                    posts.add(postTest);
                }
            }
        } else if (mode.equalsIgnoreCase("popular")) {
            postsToShow = postRepository.findPostsMostPopular(PageRequest.of(offset / limit, limit));
            postCount = (int) postsToShow.getTotalElements();
            for (Post post : postsToShow) {
                PostTest postTest = newPostTest(post);
                if (!posts.contains(postTest)) {
                    posts.add(postTest);
                }
            }
        }
//        else if () {}
//        //TODO: продолжить "best"

        allPosts.setCount(postCount);
        allPosts.setPosts(posts);
        return allPosts;
    }

    public PostResponse getSearchPostResponse (int limit, int offset, String query){
        PostResponse allPosts = new PostResponse();
        List<PostTest> posts = new ArrayList<>();
        Page<Post> postsToShow;

        int postCount = 0;

        if (query.trim().length() == 0) {
            allPosts = getAllPostResponse(limit, offset, "recent");
        } else {
            postsToShow = postRepository.findTextInPost(PageRequest.of(offset / limit, limit), query);
            postCount = (int) postsToShow.getTotalElements();
            for (Post post : postsToShow) {
                PostTest postTest = newPostTest(post);
                if (!posts.contains(postTest)) {
                    posts.add(postTest);
                }
            }
            allPosts.setCount(postCount);
            allPosts.setPosts(posts);
        }
        return allPosts;
    }

    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");

    public CalendarResponse getCalendar () {
        CalendarResponse calendar = new CalendarResponse();
        List<Integer> years = new ArrayList<>();
        HashMap<String, Integer> posts = new HashMap<>();

        List<Post> allCalendarPosts = postRepository.getCalendar();

        for (Post post : allCalendarPosts) {
            Date date = post.getTime();
            int year = Integer.parseInt(formatYear.format(date));
            String monthDay = formatDate.format(date);

            if (!years.contains(year)) {
                years.add(year);
            }
            if (!posts.containsKey(monthDay)) {
                posts.put(monthDay, 1);
            } else {
                posts.put(monthDay, posts.get(monthDay) + 1);
            }
        }
        calendar.setPosts(posts);
        calendar.setYears(years);
        return calendar;
    }

    public PostTest newPostTest (Post post) {
        PostTest postTest = new PostTest();
        postTest.setId(post.getId());
        postTest.setTimestamp(post.getTime().getTime() / 1000);
        UserTestForPostTest userTestForPostTest = new UserTestForPostTest();
        userTestForPostTest.setId(post.getUser().getId());
        userTestForPostTest.setName(post.getUser().getName());
        postTest.setUser(userTestForPostTest);
        postTest.setTitle(post.getTitle());
        String tempText = Jsoup.parse(post.getText()).text();
        if (tempText.length() > 150) {
            String announce = tempText.substring(0, 150);
            postTest.setAnnounce(announce.substring(0, announce.lastIndexOf(" ")) + "...");
        } else {
            postTest.setAnnounce(post.getText());
        }

        postTest.setLikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 1).count());
        postTest.setDislikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 0).count());
        postTest.setCommentCount(post.getPostComments().size());
        postTest.setViewCount(post.getViewCount());
        return postTest;
    }

//    public static PostResponse getPostResponse () {
//        PostResponse postResponse = new PostResponse();
//        postResponse.setCount(2);
//        List <PostTest> posts = new ArrayList<>();
//        PostTest post1 = new PostTest();
//        post1.setId(345);
//        post1.setTimestamp(new Timestamp(System.currentTimeMillis()));
//        UserTestForPostTest user1 = new UserTestForPostTest();
//        user1.setId(88);
//        user1.setName("Dmitriy Petrov");
//        post1.setUser(user1);
//        post1.setTitle("Text title");
//        post1.setAnnounce("Text announce without html-tags");
//        post1.setLikeCount(36);
//        post1.setDislikeCount(3);
//        post1.setCommentCount(15);
//        post1.setViewCount(55);
//        posts.add(post1);
//
//        PostTest post2 = new PostTest();
//        post2.setId(346);
//        post2.setTimestamp(new Timestamp(System.currentTimeMillis()));
//        UserTestForPostTest user2 = new UserTestForPostTest();
//        user2.setId(89);
//        user2.setName("Ivan Ivanov");
//        post2.setUser(user2);
//        post2.setTitle("Text title 2");
//        post2.setAnnounce("Text announce without html-tags 2");
//        post2.setLikeCount(37);
//        post2.setDislikeCount(4);
//        post2.setCommentCount(16);
//        post2.setViewCount(56);
//        posts.add(post2);
//
//        postResponse.setPosts(posts);
//
//        return postResponse;
//    }
//
//    public static PostResponse getPostEmptyResponse () {
//        PostResponse postResponse = new PostResponse();
//        postResponse.setCount(0);
//        List <PostTest> posts = new ArrayList<>();
//        postResponse.setPosts(posts);
//
//        return postResponse;
//    }
}
