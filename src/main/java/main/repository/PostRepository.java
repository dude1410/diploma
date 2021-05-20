package main.repository;

import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p " +
            "from Post p " +
            "left join User u on u.id = p.user.id " +
            "left join PostComments pc on pc.post.id = p.id " +
            "left join PostVotes pv on p.id = pc.post.id " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP) " +
            "group by p.id order by p.time desc")
    Page<Post> findPostsByTimeNewFist(Pageable pageable);

    @Query("SELECT p " +
            "from Post p " +
            "left join User u on u.id = p.user.id " +
            "left join PostComments pc on pc.post.id = p.id " +
            "left join PostVotes pv on p.id = pc.post.id " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP) " +
            "group by p.id order by p.time")
    Page<Post> findPostsByTimeOldFist(Pageable pageable);

    @Query("SELECT p " +
            "from Post p " +
            "left join User u on u.id = p.user.id " +
            "left join PostComments pc on pc.post.id = p.id " +
            "left join PostVotes pv on p.id = pc.post.id " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP) " +
            "group by p.id order by size(p.postComments) desc")
    Page<Post> findPostsMostPopular(Pageable pageable);

    @Query("SELECT p " +
            "from Post p " +
            "left join User u on u.id = p.user.id " +
            "left join PostComments pc on p.id = pc.post.id " +
            "left join PostVotes pv on p.id = pc.post.id " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP)" +
            "and (p.text like  %:query% or p.title like %:query% )" +
            "group by p.id ")
    Page<Post> findTextInPost(Pageable page, @RequestParam("query") String query);
}
