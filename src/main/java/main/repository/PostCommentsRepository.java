package main.repository;

import main.model.PostComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCommentsRepository extends JpaRepository<PostComments, Integer> {

    @Query("SELECT pc " +
            "from PostComments pc " +
            "where pc.post.id = :postId and pc.user.id = :parentId ")
    PostComments findPostCommentByPostAndParent(@Param("postId") int postId,
                                                @Param("parentId") int parentId);

    @Query("select pc.id " +
            "from PostComments pc " +
            "where pc.post.id = :postId and pc.text = :text ")
    int findPostCommentIdByPostAndText(@Param("postId") int postId,
                                       @Param("text") String text);

    @Query("select pc.id " +
            "from PostComments pc " +
            "where pc.post.id = :postId and pc.text = :text and pc.parentId = :parentId ")
    int findPostCommentIdByPostAndTextAndParent(@Param("postId") int postId,
                                                @Param("text") String text,
                                                @Param("parentId") int parentId);

    @Query("select pc " +
            "from PostComments pc " +
            "where pc.id = :parentId ")
    PostComments findById (@Param("parentId") int parentId);

}



