package main.service;

import main.api.response.TagResponse;
import main.testEntity.TagTest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagTestService {

    public static TagResponse getTagResponse() {
        TagResponse tagResponse = new TagResponse();
        List <TagTest> tags = new ArrayList<>();
        TagTest tagTest1 = new TagTest();
        tagTest1.setName("Java");
        tagTest1.setWeight(1);
        TagTest tagTest2 = new TagTest();
        tagTest2.setName("Spring");
        tagTest2.setWeight(0.56);
        TagTest tagTest3 = new TagTest();
        tagTest3.setName("Hibernate");
        tagTest3.setWeight(0.22);
        TagTest tagTest4 = new TagTest();
        tagTest4.setName("Hadoop");
        tagTest4.setWeight(0.17);
        tags.add(tagTest1);
        tags.add(tagTest2);
        tags.add(tagTest3);
        tags.add(tagTest4);
        tagResponse.setTags(tags);
        return tagResponse;
    }
}
