package main.service;

import main.api.response.TagResponse;
import main.model.Post;
import main.model.Tags;
import main.repository.PostRepository;
import main.repository.TagsRepository;
import main.model.DTO.TagTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TagsService {

    private final TagsRepository tagsRepository;
    private final PostRepository postRepository;

    @Autowired
    public TagsService(TagsRepository tagsRepository, PostRepository postRepository) {
        this.tagsRepository = tagsRepository;
        this.postRepository = postRepository;
    }

    public TagResponse getTagResponse(String query) {
        TagResponse tagResponse = new TagResponse();
        List<Tags> tags = tagsRepository.findAll();
        List<Post> posts = postRepository.findAll();
        HashMap<String, Integer> tagsCounter = new HashMap<>();

        int maxTag = 1;

        for (Tags tag : tags) {
            if (tagsCounter.containsKey(tag.getName())) {
                tagsCounter.put(tag.getName(), (tagsCounter.get(tag.getName()) + 1));
                maxTag = tagsCounter.get(tag.getName());
            } else {
                tagsCounter.put(tag.getName(), 1);
            }
        }

        double dWeightMax = (1 / ((double) maxTag / (double) posts.size()));

        List<TagTest> result = new ArrayList<>();

        for (String key : tagsCounter.keySet()) {
            TagTest tagTest = new TagTest();
            tagTest.setName(key);
            tagTest.setWeight((tagsCounter.get(key) / (double) posts.size() * dWeightMax));
            result.add(tagTest);
        }

        tagResponse.setTags(result);

        return tagResponse;
    }


}
