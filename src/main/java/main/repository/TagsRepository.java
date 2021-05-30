package main.repository;

import main.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tags, Integer> {

    @Query("select t " +
            "from Tags t " +
            "join Tag2Post tp on tp.tag.id = t.id ")

    List<Tags> findAll();

    @Query("SELECT t.id " +
            "from Tags t " +
            "where t.name = :tag ")
    int findTagIdByName (@Param("tag") String tag);

    @Query("SELECT t " +
            "from Tags t " +
            "where t.name = :tag ")
    Tags findTagByName (@Param("tag") String tag);
}