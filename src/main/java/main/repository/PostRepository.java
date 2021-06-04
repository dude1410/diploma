package main.repository;

import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
            "left join PostComments pc on pc.post.id = p.id " +
            "left join PostVotes pv on p.id = pv.post.id and pv.value = 1 " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP) " +
            "group by p.id order by count(pv) desc ")
    Page<Post> findPostsBest(Pageable page);

    @Query("SELECT p " +
            "from Post p " +
            "left join User u on u.id = p.user.id " +
            "left join PostComments pc on p.id = pc.post.id " +
            "left join PostVotes pv on p.id = pc.post.id " +
            "where (p.isActive = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= CURRENT_TIMESTAMP) " +
            "and (p.text like  %:query% or p.title like %:query% ) " +
            "group by p.id ")
    Page<Post> findTextInPost(Pageable page, @Param("query") String query);

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
    Post getPostById (@Param("id") int id);

    @Query("SELECT p " +
            "from Post p " +
            "left join Tag2Post tp on tp.post.id = p.id " +
            "left join Tags t on t.id = tp.tag.id " +
            "left join User u on u.id = p.user.id " +
            "where p.id = :id ")
    Post getPostByIdAuth (@Param("id") int id);


    @Query ("SELECT count(p) " +
            "from Post p " +
            "where p.moderationStatus = 'NEW' ")
    int findNewPosts();

    @Query("SELECT p " +
            "from Post p " +
            "left join Tag2Post tp on tp.post.id = p.id " +
            "left join Tags t on t.id = tp.tag.id " +
            "left join User u on u.id = p.user.id " +
            "where p.moderationStatus = 'NEW' " +
            "group by p.id " +
            "order by p.time desc ")
    Page<Post> getNewPostsResponse(Pageable page);

    @Query("SELECT p " +
            "from Post p " +
            "left join Tag2Post tp on tp.post.id = p.id " +
            "left join Tags t on t.id = tp.tag.id " +
            "left join User u on u.id = p.user.id " +
            "where p.moderationStatus = 'DECLINED' and p.moderatorId = :modId " +
            "group by p.id " +
            "order by p.time desc ")
    Page<Post> getDeclinedPostsResponse(Pageable page, @Param("modId") int modId);

    @Query("SELECT p " +
            "from Post p " +
            "left join Tag2Post tp on tp.post.id = p.id " +
            "left join Tags t on t.id = tp.tag.id " +
            "left join User u on u.id = p.user.id " +
            "where p.moderationStatus = 'ACCEPTED' and p.moderatorId = :modId " +
            "group by p.id " +
            "order by p.time desc ")
    Page<Post> getAcceptedPostsResponse (Pageable page, @Param("modId") int modId);

    @Query("SELECT p " +
            "from Post p " +
            "left join Tag2Post tp on tp.post.id = p.id " +
            "left join Tags t on t.id = tp.tag.id " +
            "left join User u on u.id = p.user.id " +
            "where u.id = :userId and p.isActive = 0 " +
            "group by p.id order by p.time desc ")
    Page<Post> findMyInActivePosts(Pageable page, @Param("userId") int userId);

    @Query("SELECT p " +
            "from Post p " +
            "left join Tag2Post tp on tp.post.id = p.id " +
            "left join Tags t on t.id = tp.tag.id " +
            "left join User u on u.id = p.user.id " +
            "where u.id = :userId and p.isActive = 1 and p.moderationStatus = 'NEW' " +
            "group by p.id order by p.time desc ")
    Page<Post> findMyPendingPosts(Pageable page, @Param("userId") int userId);

    @Query("SELECT p " +
            "from Post p " +
            "left join Tag2Post tp on tp.post.id = p.id " +
            "left join Tags t on t.id = tp.tag.id " +
            "left join User u on u.id = p.user.id " +
            "where u.id = :userId and p.isActive = 1 and p.moderationStatus = 'DECLINED' " +
            "group by p.id order by p.time desc ")
    Page<Post> findMyDeclinedPosts(Pageable page, @Param("userId") int userId);

    @Query("SELECT p " +
            "from Post p " +
            "left join Tag2Post tp on tp.post.id = p.id " +
            "left join Tags t on t.id = tp.tag.id " +
            "left join User u on u.id = p.user.id " +
            "where u.id = :userId and p.isActive = 1 and p.moderationStatus = 'ACCEPTED' " +
            "group by p.id order by p.time desc ")
    Page<Post> findMyAcceptedPosts(Pageable page, @Param("userId") int userId);

    @Query("SELECT p " +
            "from Post p " +
            "where p.id = :id ")
    Post getPostByIdModerate (@Param("id") int id);

    @Query("SELECT p " +
            "from Post p " +
            "left join PostVotes pv on pv.post.id = p.id " +
            "left join User u on u.id = p.user.id " +
            "where u.email = :email " +
            "group by p.id " +
            "order by p.time ")
    List<Post> findAllMyPostsByEmail (@Param("email") String email);

    @Query("SELECT p " +
            "from Post p " +
            "left join PostVotes pv on pv.post.id = p.id " +
            "group by p.id " +
            "order by p.time ")
    List<Post> allPosts ();
}