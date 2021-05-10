package main.api.response;

import main.testEntity.PostTest;
import org.springframework.stereotype.Component;

@Component
public class PostResponse {

    private int count = 1;
//    private int count = 0;
    private PostTest postTest = new PostTest();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public PostTest getPostTest() {
        return postTest;
    }

    public void setPostTest(PostTest postTest) {
        this.postTest = postTest;
    }
}
