package main.service;

import main.api.response.CalendarResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CalendarService {

    public static CalendarResponse getCalendarResponse(){
        CalendarResponse calenderResponse = new CalendarResponse();
        HashMap <String, Integer> posts = new HashMap<>();
        posts.put("2019-12-17", 56);
        posts.put("2019-12-14", 11);
        posts.put("2019-06-17", 1);
        posts.put("2019-03-12", 6);
        calenderResponse.setPosts(posts);
        List<Integer> years = new ArrayList<>();
        years.add(2017);
        years.add(2018);
        years.add(2019);
        years.add(2020);
        calenderResponse.setYears(years);
        return calenderResponse;
    }
}
