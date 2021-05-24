package main.repository;

import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p " +
            "from Post p " +
            "left join User u on u.id = p.user.id " +
            "left join PostComments pc on pc.post.id = p.id " +
            "left join PostVotes pv on p.id = pc.post.id " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP) " +
            "group by p.id order by p.time desc ")
    Page<Post> findPostsByTimeNewFist(Pageable page);

    @Query("SELECT p " +
            "from Post p " +
            "left join User u on u.id = p.user.id " +
            "left join PostComments pc on pc.post.id = p.id " +
            "left join PostVotes pv on p.id = pc.post.id " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP) " +
            "group by p.id order by p.time ")
    Page<Post> findPostsByTimeOldFist(Pageable page);

    @Query("SELECT p " +
            "from Post p " +
            "left join User u on u.id = p.user.id " +
            "left join PostComments pc on pc.post.id = p.id " +
            "left join PostVotes pv on p.id = pc.post.id " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP) " +
            "group by p.id order by size(p.postComments) desc ")
    Page<Post> findPostsMostPopular(Pageable page);

    @Query("SELECT p " +
            "from Post p " +
            "left join User u on u.id = p.user.id " +
            "left join PostComments pc on p.id = pc.post.id " +
            "left join PostVotes pv on p.id = pc.post.id " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP) " +
            "and (p.text like  %:query% or p.title like %:query% ) " +
            "group by p.id ")
    Page<Post> findTextInPost(Pageable page, @RequestParam("query") String query);

    @Query("SELECT p " +
            "from Post p " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP) " +
            "group by p.id order by p.time desc ")
    List<Post> getCalendar();

    @Query("SELECT p " +
            "from Post p " +
            "where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP and " +
            "DATE_FORMAT(p.time, '%Y-%m-%d') = str(:date) ")
    Page<Post> getSearchByDatePostResponse(Pageable page, @Param("date") String date);

    @Query("SELECT p " +
            "from Post p " +
            "left join Tag2Post tp on tp.post.id = p.id " +
            "left join Tags t on t.id = tp.tag.id " +
            "where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP " +
            "and t.name like %:tag% ")
    Page<Post> getSearchByTagPostResponse(Pageable page, @Param("tag") String tag);

    @Query("SELECT p " +
            "from Post p " +
            "left join Tag2Post tp on tp.post.id = p.id " +
            "left join Tags t on t.id = tp.tag.id " +
            "left join User u on u.id = p.user.id " +
            "where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= current_timestamp " +
            "and p.id = :id ")
    Post getPostById (int id);
}