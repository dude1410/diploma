package main.api.response;

import main.model.DTO.TagTDO;

import java.util.List;

public class TagResponse {

    private List <TagTDO> tags;

    public List<TagTDO> getTags() {
        return tags;
    }

    public void setTags(List<TagTDO> tags) {
        this.tags = tags;
    }
}
