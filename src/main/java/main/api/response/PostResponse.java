package main.api.response;

import main.model.DTO.PostTDO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {

    private int count;
    private List<PostTDO> posts = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostTDO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostTDO> posts) {
        this.posts = posts;
    }
}
