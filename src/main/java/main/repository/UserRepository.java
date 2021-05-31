package main.repository;

import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.email = :email ")
    User findByEmail(@Param("email") String email);

    @Query("select u " +
            "from User u " +
            "where u.email = :email and u.isModerator = 1 ")
    User findModeratorByEmail(@Param("email") String email);

    @Query("select u " +
            "from User u " +
            "where u.code = :code ")
    User findByCode (@Param("code") String code);
}
