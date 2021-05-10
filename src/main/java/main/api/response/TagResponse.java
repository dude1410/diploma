package main.api.response;

import main.testEntity.TagTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagResponse {

    @Autowired
    private List <TagTest> tagTestList;

    public List<TagTest> getTagTestList() {
        return tagTestList;
    }

    public void setTagTestList(List<TagTest> tagTestList) {
        this.tagTestList = tagTestList;
    }
}
