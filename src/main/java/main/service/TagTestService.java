package main.service;

import main.api.response.TagResponse;
import main.testEntity.TagTest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagTestService {

    public static TagResponse getTagResponse() {
        TagResponse tagResponse = new TagResponse();
        List <TagTest> tagTests = tagResponse.getTagTestList();
        TagTest tagTest1 = new TagTest();
        tagTest1.setName("Java");
        tagTest1.setWeight(1);
        TagTest tagTest2 = new TagTest();
        tagTest1.setName("Spring");
        tagTest1.setWeight(0.56);
        TagTest tagTest3 = new TagTest();
        tagTest1.setName("Hibernate");
        tagTest1.setWeight(0.22);
        TagTest tagTest4 = new TagTest();
        tagTest1.setName("Hadoop");
        tagTest1.setWeight(0.17);
        tagTests.add(tagTest1);
        tagTests.add(tagTest2);
        tagTests.add(tagTest3);
        tagTests.add(tagTest4);
        return tagResponse;
    }
}
