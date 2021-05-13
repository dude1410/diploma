package main.api.response;

import main.testEntity.PostTest;

import java.util.ArrayList;
import java.util.List;

public class PostResponse {

    private int count = 1;
//    private int count = 0;
    private List<PostTest> posts = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostTest> getPosts() {
        return posts;
    }

    public void setPosts(List<PostTest> posts) {
        this.posts = posts;
    }
}
