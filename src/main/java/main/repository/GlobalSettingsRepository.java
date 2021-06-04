package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Integer> {

    @Query("select st.value " +
            "from GlobalSettings st " +
            "where st.code = 'STATISTICS_IS_PUBLIC' ")
    String findStatisticsIsPublic ();

    @Query("select st.value " +
            "from GlobalSettings st " +
            "where st.code = 'MULTIUSER_MODE' ")
    String findMultiuserMode ();

    @Query("select st.value " +
            "from GlobalSettings st " +
            "where st.code = 'POST_PREMODERATION' ")
    String findPremoderationMode ();



}