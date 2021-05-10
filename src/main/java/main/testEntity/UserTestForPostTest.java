package main.testEntity;

import org.springframework.stereotype.Component;

@Component
public class UserTestForPostTest {
    private int id = 88;
    private String name = "Dmitriy Petrov";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
