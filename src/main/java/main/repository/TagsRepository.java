package main.repository;

import main.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tags, Integer> {

    @Query("select t " +
            "from Tags t " +
            "join Tag2Post tp on tp.tag.id = t.id ")

    List<Tags> findAll();
}