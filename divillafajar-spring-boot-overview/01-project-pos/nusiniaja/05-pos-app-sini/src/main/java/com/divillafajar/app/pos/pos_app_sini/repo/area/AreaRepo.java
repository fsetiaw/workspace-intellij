package com.divillafajar.app.pos.pos_app_sini.repo.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.ClientAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.SpaceAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaSummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.SpaceAreaHierarchyProjection;
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
        SELECT\s
          sa.client_address_id AS clientAddressId,
          SUM(CASE WHEN sa.parent_id IS NULL THEN 1 ELSE 0 END) AS totalTopParent,
          SUM(
              CASE\s
                  WHEN sa.parent_id IS NOT NULL\s
                       AND sa.id NOT IN (
                          SELECT DISTINCT s2.parent_id\s
                          FROM space_area s2\s
                          WHERE s2.parent_id IS NOT NULL
                       )
                  THEN 1 ELSE 0\s
              END
          ) AS totalEndChild
      FROM space_area sa
      JOIN client_address ca ON sa.client_address_id = ca.id
      WHERE ca.pub_id = :pubId
      GROUP BY sa.client_address_id;
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

	@Query(
			value = """
            WITH RECURSIVE area_hierarchy AS (
                SELECT
                    sa.id,
                    sa.name,
                    sa.client_address_id,
                    sa.parent_id,
                    LOWER(sa.name) AS sort_key,
                    0 AS indent_level,
                    CAST(LOWER(sa.name) AS CHAR(255)) AS path
                FROM space_area sa
                WHERE sa.client_address_id = :clientAddressId
                  AND sa.parent_id IS NULL
                  AND sa.deleted = FALSE

                UNION ALL

                SELECT
                    c.id,
                    c.name,
                    c.client_address_id,
                    c.parent_id,
                    LOWER(c.name) AS sort_key,
                    ah.indent_level + 1 AS indent_level,
                    CONCAT(ah.path, '>', LOWER(c.name)) AS path
                FROM space_area c
                INNER JOIN area_hierarchy ah ON c.parent_id = ah.id
                WHERE c.deleted = FALSE
            )
            SELECT
                id,
                name,
                client_address_id,
                parent_id,
                indent_level
            FROM area_hierarchy
            ORDER BY
                SUBSTRING_INDEX(path, '>', 1),
                path
            """,
			nativeQuery = true
	)
	List<SpaceAreaHierarchyProjection> findAllByClientAddressHierarchical(
			@Param("clientAddressId") Long clientAddressId
	);

}
