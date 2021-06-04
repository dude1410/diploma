package main.repository;

import main.model.CaptchaCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Repository
public interface CaptchaCodesRepository extends JpaRepository<CaptchaCodes, Integer> {

    @Modifying
    void deleteAllByTimeBefore(Date date);

    @Query("select cc " +
            "from CaptchaCodes cc " +
            "where cc.secretCode = :secretCode ")
    CaptchaCodes findBySecretCode(@Param("secretCode") String secretCode);
}
