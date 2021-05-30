package main.repository;

import main.model.Tag2Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Tag2PostRepository extends JpaRepository<Tag2Post, Integer> {

    @Query("SELECT tp " +
            "from Tag2Post tp " +
            "where tp.post.id = :postId and tp.tag.id = :tagId ")
    Tag2Post findByPostAndTag (@Param("postId") int postId, @Param("tagId") int tagId);

}
