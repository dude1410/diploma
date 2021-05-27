package main.service;

import main.api.response.CalendarResponse;
import main.api.response.PostResponse;
import main.api.response.PostResponseId;
import main.model.Post;
import main.model.PostComments;
import main.model.Tags;
import main.repository.PostRepository;
import main.testEntity.CommentTestForPost;
import main.testEntity.PostTest;
import main.testEntity.UserTestForCommentForPost;
import main.testEntity.UserTestForPostTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.*;

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
        else if (mode.equalsIgnoreCase("best")) {
            postsToShow = postRepository.findPostsBest(PageRequest.of(offset / limit, limit));
            postCount = (int) postsToShow.getTotalElements();
            for (Post post : postsToShow) {
                PostTest postTest = newPostTest(post);
                if (!posts.contains(postTest)) {
                    posts.add(postTest);
                }
            }
        }

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

    public PostResponse getSearchByDatePostResponse (int limit, int offset, String date) {
        PostResponse allPosts = new PostResponse();
        List<PostTest> posts = new ArrayList<>();
        Page<Post> postsToShow;

        int postCount = 0;

        postsToShow = postRepository.getSearchByDatePostResponse(PageRequest.of(offset / limit, limit), date);
        postCount = (int) postsToShow.getTotalElements();
        for (Post post : postsToShow) {
            PostTest postTest = newPostTest(post);
            if (!posts.contains(postTest)) {
                posts.add(postTest);
            }
        }
        allPosts.setCount(postCount);
        allPosts.setPosts(posts);
        return allPosts;
    }

    public PostResponse getSearchByTagPostResponse (int limit, int offset, String tag){
        PostResponse allPosts = new PostResponse();
        List<PostTest> posts = new ArrayList<>();
        Page<Post> postsToShow;

        int postCount = 0;

        postsToShow = postRepository.getSearchByTagPostResponse(PageRequest.of(offset / limit, limit), tag);
        postCount = (int) postsToShow.getTotalElements();
        for (Post post : postsToShow) {
            PostTest postTest = newPostTest(post);
            if (!posts.contains(postTest)) {
                posts.add(postTest);
            }
        }
        allPosts.setCount(postCount);
        allPosts.setPosts(posts);

        return allPosts;
    }
    // todo: добавить код после авторизации
    // todo: изменить тип возврата для active
    public PostResponseId getPostById(int id){
        PostResponseId postResponseId = new PostResponseId();
        Post post = postRepository.getPostById(id);
        if (post == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        postResponseId.setId(post.getId());
        postResponseId.setTimestamp(post.getTime().getTime() / 1000);
        if (post.isIs_active() == 1) {
            postResponseId.setActive(true);
        } else {postResponseId.setActive(false);}
        UserTestForPostTest user = new UserTestForPostTest();
        user.setId(post.getUser().getId());
        user.setName(post.getUser().getName());
        postResponseId.setUser(user);
        postResponseId.setTitle(post.getTitle());
        postResponseId.setText(post.getText());
        postResponseId.setLikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 1).count());
        postResponseId.setDislikeCount((int) post.getPostVotes().stream().filter(a -> a.getValue() == 0).count());
        postResponseId.setViewCount(post.getViewCount());
        List<CommentTestForPost> comments = new ArrayList<>();
        Set<PostComments> postComments = post.getPostComments();
        for (PostComments com : postComments){
            CommentTestForPost comment = new CommentTestForPost();
            comment.setId(com.getId());
            comment.setTimestamp(com.getTime().getTime() / 1000);
            comment.setText(com.getText());
            UserTestForCommentForPost userTestForPostTest = new UserTestForCommentForPost();
            userTestForPostTest.setId(com.getUser().getId());
            userTestForPostTest.setName(com.getUser().getName());
            userTestForPostTest.setPhoto(com.getUser().getPhoto());
            comment.setUser(userTestForPostTest);
            comments.add(comment);
        }
        comments.sort(Comparator.comparing(CommentTestForPost::getTimestamp).reversed());
        postResponseId.setComments(comments);
        Set<Tags> tagSet = post.getTags();
        List<String> tags = new ArrayList<>();
        for (Tags tag : tagSet) {
            tags.add(tag.getName());
        }
        postResponseId.setTags(tags);
        return postResponseId;
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
}
