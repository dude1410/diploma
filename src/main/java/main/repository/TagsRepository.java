package main.repository;

import main.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tags, Integer> {

    @Query ("select t " +
            "from Tags t " +
            "left join Tag2Post tp on tp.tag.id = t.id " +
            "left join Post p on p.id = tp.post.id ")
    List<Tags> findAll();
}