package com.divillafajar.app.pos.pos_app_sini.repo.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.ClientAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.SpaceAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaSummaryProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaRepo extends CrudRepository<SpaceAreaEntity, Long> {
	@Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM space_area sa
        JOIN client_address ca ON sa.client_address_id = ca.id
        WHERE ca.pub_id = :pubId
    """, nativeQuery = true)
	Long hasAreaByClientAddressPubId(@Param("pubId") String pubId);

	@Query(value = """
        SELECT 
            sa.client_address_id AS clientAddressId,
            SUM(CASE WHEN sa.parent_id IS NULL THEN 1 ELSE 0 END) AS totalTopParent,
            SUM(CASE WHEN sa.id NOT IN (
                SELECT DISTINCT s2.parent_id FROM space_area s2 WHERE s2.parent_id IS NOT NULL
            ) THEN 1 ELSE 0 END) AS totalEndChild
        FROM space_area sa
        JOIN client_address ca ON sa.client_address_id = ca.id
        WHERE ca.pub_id = :pubId
        GROUP BY sa.client_address_id
    """, nativeQuery = true)
	AreaSummaryProjection getSpaceAreaSummaryByClientAddressPubId(@Param("pubId") String pubId);

	@Query("""
        SELECT CASE WHEN COUNT(sa) > 0 THEN TRUE ELSE FALSE END
        FROM SpaceAreaEntity sa
        JOIN sa.clientAddress ca
        WHERE LOWER(sa.name) = LOWER(:name)
          AND ca.pubId = :clientAddressPubId
          AND sa.deleted = false
    """)
	boolean existsByNameInSpaceArea(@Param("clientAddressPubId") String clientAddressPubId,
	                                @Param("name") String name);


	// 🔹 Gabungan langsung (cek di kedua tabel sekaligus)
	@Query(value = """
    SELECT CASE WHEN (
        (SELECT COUNT(*) FROM space_area sa
         JOIN client_address ca ON sa.client_address_id = ca.id
         WHERE LOWER(sa.name) = LOWER(:name)
           AND ca.pub_id = :clientAddressPubId
           AND sa.deleted = 0)
        +
        (SELECT COUNT(*) FROM guest_area ga
         JOIN client_address ca ON ga.client_address_id = ca.id
         WHERE LOWER(ga.name) = LOWER(:name)
           AND ca.pub_id = :clientAddressPubId
           AND ga.deleted = 0)
    ) > 0 THEN TRUE ELSE FALSE END AS exists_flag
""", nativeQuery = true)
	Long existsByNameInAnyArea(@Param("clientAddressPubId") String clientAddressPubId,
	                              @Param("name") String name);

}
