package main.repository;

import main.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {

    @Query("select pv " +
            "from PostVotes pv " +
            "where pv.user.id = :userId and pv.post.id = :postId ")
    PostVotes findByUserAndPost(@Param("userId")int userId, @Param("postId")int postId);
}
