package main.api.response;

import main.testEntity.TagTest;

import java.util.List;

public class TagResponse {

    private List <TagTest> tags;

    public List<TagTest> getTags() {
        return tags;
    }

    public void setTags(List<TagTest> tags) {
        this.tags = tags;
    }
}
