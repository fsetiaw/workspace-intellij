package com.divillafajar.app.pos.pos_app_sini.repo.feature;

import com.divillafajar.app.pos.pos_app_sini.io.entity.fitur.FeatureEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FeatureRepo extends CrudRepository<FeatureEntity,Long> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO feature (feature_name, avail_time, active)
        SELECT :featureName, NOW(), true
        WHERE NOT EXISTS (
            SELECT 1 FROM feature WHERE feature_name = :featureName
        )
        """, nativeQuery = true)
    int insertIfNotExists(@Param("featureName") String featureName);

    Optional<FeatureEntity> findByFeatureName(String featureName);

    @Query(value = "SELECT id FROM feature WHERE feature_name = :featureName", nativeQuery = true)
    Optional<Long> findIdByFeatureName(@Param("featureName") String featureName);
}
