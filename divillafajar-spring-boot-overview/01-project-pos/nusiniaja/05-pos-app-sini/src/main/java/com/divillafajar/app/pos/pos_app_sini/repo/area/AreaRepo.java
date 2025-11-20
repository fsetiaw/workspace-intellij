package com.divillafajar.app.pos.pos_app_sini.repo.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.ClientAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.SpaceAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.projection.CategoryHierarchyProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaHierarchyProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaSummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.SpaceAreaHierarchyProjection;
import com.divillafajar.app.pos.pos_app_sini.model.product.ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaRepo extends CrudRepository<SpaceAreaEntity, Long> {

    boolean existsByNameIgnoreCaseAndClientAddress_IdAndDeletedFalse(
            String name,
            Long clientAddressId
    );

    @Query(value = """
        WITH RECURSIVE area_hierarchy AS (
          SELECT 
            id,
            name,
            parent_id,
            id AS top_parent_id,
            name AS top_parent_name,
            0 AS level
          FROM space_area
          WHERE parent_id IS NULL

          UNION ALL

          SELECT 
            c.id,
            c.name,
            c.parent_id,
            ch.top_parent_id,
            ch.top_parent_name,
            ch.level + 1 AS level
          FROM space_area c
          INNER JOIN area_hierarchy ch ON c.parent_id = ch.id
        )
        SELECT id, name, top_parent_id, top_parent_name, level
        FROM area_hierarchy
        WHERE id = :id
        """, nativeQuery = true)
    AreaHierarchyProjectionDTO findCategoryHierarchyLevelById(@Param("id") Long id);

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


    @Query(
            value = """
            WITH RECURSIVE category_path AS (
              SELECT
                id,
                name,
                parent_id,
                client_address_id,
                CAST(CONCAT(REPLACE(name, '/', '|'), '~', id) AS CHAR(1000)) AS full_path
              FROM space_area
              WHERE parent_id IS NULL
                AND client_address_id = :clientAddressId
    
              UNION ALL
    
              SELECT
                c.id,
                c.name,
                c.parent_id,
                c.client_address_id,
                CONCAT(cp.full_path, ' / ', REPLACE(c.name, '/', '|'), '~', c.id) AS full_path
              FROM space_area c
              INNER JOIN category_path cp ON cp.id = c.parent_id
            )
    
            SELECT 
              cp.full_path,
              cp.id AS category_id,
              COUNT(p.id) AS total_products
            FROM category_path cp
            LEFT JOIN product p
              ON p.category_id = cp.id
              AND p.deleted = FALSE
              AND p.client_address_id = :clientAddressId
            WHERE NOT EXISTS (
              SELECT 1 FROM space_area child WHERE child.parent_id = cp.id
            )
            GROUP BY cp.id, cp.full_path
            ORDER BY cp.full_path;                 
    """,
            nativeQuery = true
    )
    List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> findAllPathEndCategoryChildHierarchical(@Param("clientAddressId") Long clientAddressId);


    @Query(
            value = """
            WITH RECURSIVE category_path AS (
             SELECT
               id,
               name,
               parent_id,
               client_address_id,
               CAST(CONCAT(REPLACE(name, '/', '|'), '~', id) AS CHAR(1000)) AS full_path
             FROM space_area
             WHERE parent_id IS NULL
               AND client_address_id = :clientAddressId

             UNION ALL

             SELECT
               c.id,
               c.name,
               c.parent_id,
               c.client_address_id,
               CONCAT(cp.full_path, ' / ', REPLACE(c.name, '/', '|'), '~', c.id) AS full_path
             FROM space_area c
             INNER JOIN category_path cp ON cp.id = c.parent_id
           ),
           category_with_total AS (
             SELECT
               cp.full_path,
               cp.id AS category_id,
               COUNT(p.id) AS total_products
             FROM category_path cp
             LEFT JOIN product p
               ON p.category_id = cp.id
               AND p.deleted = FALSE
               AND p.client_address_id = :clientAddressId
             WHERE NOT EXISTS (
               SELECT 1 FROM space_area child WHERE child.parent_id = cp.id
             )
             GROUP BY cp.id, cp.full_path
           )
           SELECT *
           FROM category_with_total
           WHERE total_products < 1
           ORDER BY full_path;
    """,
            nativeQuery = true
    )
    List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> findAllPathEndCategoryChildHierarchicalHasNoItemOnly(@Param("clientAddressId") Long clientAddressId);

    @Query(
            value = """
            WITH RECURSIVE category_path AS (
             SELECT
               id,
               name,
               parent_id,
               client_address_id,
               CAST(CONCAT(REPLACE(name, '/', '|'), '~', id) AS CHAR(1000)) AS full_path
             FROM space_area
             WHERE parent_id IS NULL
               AND client_address_id = :clientAddressId

             UNION ALL

             SELECT
               c.id,
               c.name,
               c.parent_id,
               c.client_address_id,
               CONCAT(cp.full_path, ' / ', REPLACE(c.name, '/', '|'), '~', c.id) AS full_path
             FROM space_area c
             INNER JOIN category_path cp ON cp.id = c.parent_id
           ),
           category_with_total AS (
             SELECT
               cp.full_path,
               cp.id AS category_id,
               COUNT(p.id) AS total_products
             FROM category_path cp
             LEFT JOIN product p
               ON p.category_id = cp.id
               AND p.deleted = FALSE
               AND p.client_address_id = :clientAddressId
             WHERE NOT EXISTS (
               SELECT 1 FROM space_area child WHERE child.parent_id = cp.id
             )
             GROUP BY cp.id, cp.full_path
           )
           SELECT *
           FROM category_with_total
           WHERE total_products > 0
           ORDER BY full_path;
    """,
            nativeQuery = true
    )
    List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> findAllPathEndCategoryChildHierarchicalHasItemOnly(@Param("clientAddressId") Long clientAddressId);


    @Query(
            value = """
        WITH RECURSIVE category_path AS (
          SELECT
            id,
            name,
            parent_id,
            client_address_id,
            CAST(CONCAT(REPLACE(name, '/', '|'), '~', id) AS CHAR(1000)) AS full_path
          FROM space_area
          WHERE parent_id IS NULL
            AND client_address_id = :clientAddressId

          UNION ALL

          SELECT
            c.id,
            c.name,
            c.parent_id,
            c.client_address_id,
            CONCAT(cp.full_path, ' / ', REPLACE(c.name, '/', '|'), '~', c.id) AS full_path
          FROM space_area c
          INNER JOIN category_path cp ON cp.id = c.parent_id
        ),
        category_with_missing_thumb AS (
          SELECT\s
            cp.full_path,
            cp.id AS category_id,
            COUNT(p.id) AS total_products_without_thumb
          FROM category_path cp
          LEFT JOIN product p
            ON p.category_id = cp.id
            AND p.deleted = FALSE
            AND p.client_address_id = :clientAddressId
            AND (p.thumbnail_url IS NULL OR TRIM(p.thumbnail_url) = '')
          WHERE NOT EXISTS (
            SELECT 1 FROM space_area child WHERE child.parent_id = cp.id
          )
          GROUP BY cp.id, cp.full_path
        )
        SELECT *
        FROM category_with_missing_thumb
        WHERE total_products_without_thumb > 0
        ORDER BY full_path;
    """,
            nativeQuery = true
    )
    List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> findAllPathEndCategoryChildHierarchicalHasItemOnlyButNoImage(@Param("clientAddressId") Long clientAddressId);

    @Query(
            value = """
        WITH RECURSIVE category_path AS (
          SELECT
            id,
            name,
            parent_id,
            client_address_id,
            CAST(CONCAT(REPLACE(name, '/', '|'), '~', id) AS CHAR(1000)) AS full_path
          FROM space_area
          WHERE parent_id IS NULL
            AND client_address_id = :clientAddressId

          UNION ALL

          SELECT
            c.id,
            c.name,
            c.parent_id,
            c.client_address_id,
            CONCAT(cp.full_path, ' / ', REPLACE(c.name, '/', '|'), '~', c.id) AS full_path
          FROM space_area c
          INNER JOIN category_path cp ON cp.id = c.parent_id
        ),
        category_with_missing_thumb AS (
          SELECT\s
            cp.full_path,
            cp.id AS category_id,
            COUNT(p.id) AS total_products_without_thumb
          FROM category_path cp
          LEFT JOIN product p
            ON p.category_id = cp.id
            AND p.deleted = FALSE
            AND p.client_address_id = :clientAddressId
            AND (p.price IS NULL OR price<=0)
          WHERE NOT EXISTS (
            SELECT 1 FROM space_area child WHERE child.parent_id = cp.id
          )
          GROUP BY cp.id, cp.full_path
        )
        SELECT *
        FROM category_with_missing_thumb
        WHERE total_products_without_thumb > 0
        ORDER BY full_path;
    """,
            nativeQuery = true
    )
    List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> findAllPathEndCategoryChildHierarchicalHasItemOnlyButNoPrice(@Param("clientAddressId") Long clientAddressId);

    @Query(
            value = """
        WITH RECURSIVE category_path AS (
          SELECT
            id,
            name,
            parent_id,
            client_address_id,
            CAST(CONCAT(REPLACE(name, '/', '|'), '~', id) AS CHAR(1000)) AS full_path
          FROM space_area
          WHERE parent_id IS NULL
            AND client_address_id = :clientAddressId

          UNION ALL

          SELECT
            c.id,
            c.name,
            c.parent_id,
            c.client_address_id,
            CONCAT(cp.full_path, ' / ', REPLACE(c.name, '/', '|'), '~', c.id) AS full_path
          FROM space_area c
          INNER JOIN category_path cp ON cp.id = c.parent_id
        ),
        category_with_missing_thumb AS (
          SELECT\s
            cp.full_path,
            cp.id AS category_id,
            COUNT(p.id) AS total_products_without_thumb
          FROM category_path cp
          LEFT JOIN product p
            ON p.category_id = cp.id
            AND p.deleted = FALSE
            AND p.client_address_id = :clientAddressId
            AND (p.description IS NULL OR TRIM(p.description) = '')
          WHERE NOT EXISTS (
            SELECT 1 FROM space_area child WHERE child.parent_id = cp.id
          )
          GROUP BY cp.id, cp.full_path
        )
        SELECT *
        FROM category_with_missing_thumb
        WHERE total_products_without_thumb > 0
        ORDER BY full_path;
    """,
            nativeQuery = true
    )
    List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> findAllPathEndCategoryChildHierarchicalHasItemOnlyButNoDesc(@Param("clientAddressId") Long clientAddressId);

	/******************************ID***************************************/
	@Modifying
	@Query(value = """
        INSERT INTO space_area (name, indent_level, parent_id, client_address_id, created_by, deleted, created_at)
       SELECT tmp.name, tmp.indent_level, 
        -- Ambil parent_id yang masih aktif (deleted = FALSE)
        (
            SELECT pc2.id 
            FROM space_area pc2
            WHERE pc2.client_address_id = :clientAddressId
              AND pc2.deleted = FALSE 
              AND LOWER(pc2.name) = LOWER(tmp.name)
            LIMIT 1
        ) AS parent_id,
        tmp.client_address_id, tmp.created_by, tmp.deleted, NOW()
       FROM (
           SELECT 'Akomodasi' AS name, NULL AS indent_level, NULL AS parent_id, :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted, NOW() as created_at
           UNION ALL SELECT 'Area Parkir', NULL, NULL, :clientAddressId, 'system', 0, NOW()
           UNION ALL SELECT 'Food & Beverages Area', NULL, NULL, :clientAddressId, 'system', 0, NOW()
           UNION ALL SELECT 'Fasilitas Publik', NULL, NULL, :clientAddressId, 'system', 0, NOW()
           UNION ALL SELECT 'Security Area', NULL, NULL, :clientAddressId, 'system', 0, NOW()
       ) AS tmp
       WHERE NOT EXISTS (
           SELECT 1 FROM space_area pc
           WHERE LOWER(pc.name) = LOWER(tmp.name)
           AND pc.client_address_id = :clientAddressId 
           AND pc.deleted = FALSE -- hanya cek yang aktif
       );
    """, nativeQuery = true)
	void insertDefaultIdArea(@Param("clientAddressId") Long clientAddressId);

	@Modifying
	@Query(value = """
      INSERT INTO space_area (name, indent_level, parent_id, client_address_id, created_by, deleted, created_at)
          SELECT * FROM (
              SELECT 'Outdoor' AS name, 1 AS indent_level,
                     (
                         SELECT id FROM space_area 
                         WHERE LOWER(name) = 'Area Parkir'
                           AND client_address_id = :clientAddressId
                           AND deleted = FALSE                   -- hanya parent aktif
                         LIMIT 1
                     ) AS parent_id,
                     :clientAddressId AS client_address_id,
                     'system' AS created_by,
                     0 AS deleted, NOW() AS created_at

              UNION ALL
              SELECT 'Carport', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Area Parkir'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
            
              UNION ALL
              SELECT 'Basement', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Area Parkir'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
          ) AS tmp
          WHERE NOT EXISTS (
              SELECT 1 
              FROM space_area pc
              WHERE LOWER(pc.name) = LOWER(tmp.name)
                AND pc.client_address_id = :clientAddressId
                AND pc.deleted = FALSE       -- hanya kategori aktif yang dianggap exist
          );
            
    """, nativeQuery = true)
	void insertDefaultIdSubPark(@Param("clientAddressId") Long clientAddressId);

	@Modifying
	@Query(value = """
      INSERT INTO space_area (name, indent_level, parent_id, client_address_id, created_by, deleted, created_at)
          SELECT * FROM (
              SELECT 'Resto' AS name, 1 AS indent_level,
                     (
                         SELECT id FROM space_area 
                         WHERE LOWER(name) = 'Food & Beverages Area'
                           AND client_address_id = :clientAddressId
                           AND deleted = FALSE                   -- hanya parent aktif
                         LIMIT 1
                     ) AS parent_id,
                     :clientAddressId AS client_address_id,
                     'system' AS created_by,
                     0 AS deleted, NOW() AS created_at

              UNION ALL
              SELECT 'Cafe', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Food & Beverages Area'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
                     
              UNION ALL
              SELECT 'Mart', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Food & Beverages Area'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
          ) AS tmp
          WHERE NOT EXISTS (
              SELECT 1 
              FROM space_area pc
              WHERE LOWER(pc.name) = LOWER(tmp.name)
                AND pc.client_address_id = :clientAddressId
                AND pc.deleted = FALSE       -- hanya kategori aktif yang dianggap exist
          );
            
    """, nativeQuery = true)
	void insertDefaultIdSubFB(@Param("clientAddressId") Long clientAddressId);



	@Modifying
	@Query(value = """
      INSERT INTO space_area (name, indent_level, parent_id, client_address_id, created_by, deleted, created_at)
          SELECT * FROM (
              SELECT 'Pos Jaga' AS name, 1 AS indent_level,
                     (
                         SELECT id FROM space_area 
                         WHERE LOWER(name) = 'Security Area'
                           AND client_address_id = :clientAddressId
                           AND deleted = FALSE                   -- hanya parent aktif
                         LIMIT 1
                     ) AS parent_id,
                     :clientAddressId AS client_address_id,
                     'system' AS created_by,
                     0 AS deleted, NOW() AS created_at

              UNION ALL
              SELECT 'CCTV', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Security Area'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
          ) AS tmp
          WHERE NOT EXISTS (
              SELECT 1 
              FROM space_area pc
              WHERE LOWER(pc.name) = LOWER(tmp.name)
                AND pc.client_address_id = :clientAddressId
                AND pc.deleted = FALSE       -- hanya kategori aktif yang dianggap exist
          );
            
    """, nativeQuery = true)
	void insertDefaultIdSubSecurity(@Param("clientAddressId") Long clientAddressId);

	@Modifying
	@Query(value = """
      INSERT INTO space_area (name, indent_level, parent_id, client_address_id, created_by, deleted, created_at)
          SELECT * FROM (
              SELECT 'Campground' AS name, 1 AS indent_level,
                     (
                         SELECT id FROM space_area 
                         WHERE LOWER(name) = 'Akomodasi'
                           AND client_address_id = :clientAddressId
                           AND deleted = FALSE                   -- hanya parent aktif
                         LIMIT 1
                     ) AS parent_id,
                     :clientAddressId AS client_address_id,
                     'system' AS created_by,
                     0 AS deleted, NOW() AS created_at

              UNION ALL
              SELECT 'Villa', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Akomodasi'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()

              UNION ALL
              SELECT 'Cabin', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Akomodasi'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()

              UNION ALL
              SELECT 'Bungalow / Cottage', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Akomodasi'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
                     
             UNION ALL
             SELECT 'Glamping', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Akomodasi'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
                     
             UNION ALL
             SELECT 'Hostel / Homestay', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Akomodasi'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
          ) AS tmp
          WHERE NOT EXISTS (
              SELECT 1 
              FROM space_area pc
              WHERE LOWER(pc.name) = LOWER(tmp.name)
                AND pc.client_address_id = :clientAddressId
                AND pc.deleted = FALSE       -- hanya kategori aktif yang dianggap exist
          );
            
    """, nativeQuery = true)
	void insertDefaultIdSubAccomodation(@Param("clientAddressId") Long clientAddressId);

	@Modifying
	@Query(value = """
      INSERT INTO space_area (name, indent_level, parent_id, client_address_id, created_by, deleted, created_at)
          SELECT * FROM (
              SELECT 'Lobby / Front Desk' AS name, 1 AS indent_level,
                     (
                         SELECT id FROM space_area 
                         WHERE LOWER(name) = 'Fasilitas Publik'
                           AND client_address_id = :clientAddressId
                           AND deleted = FALSE                   -- hanya parent aktif
                         LIMIT 1
                     ) AS parent_id,
                     :clientAddressId AS client_address_id,
                     'system' AS created_by, 
                     0 AS deleted, NOW() AS created_at

              UNION ALL
              SELECT 'Restroom', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Fasilitas Publik'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()

              UNION ALL
              SELECT 'Prayer Room', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Fasilitas Publik'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()

              UNION ALL
              SELECT 'Garden', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Fasilitas Publik'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
                     
             UNION ALL
             SELECT 'Playground Area', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Fasilitas Publik'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
                     
             UNION ALL
             SELECT 'Sport Area', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Fasilitas Publik'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
                     
             UNION ALL
             SELECT 'Dapur Umum', 1,
                     (SELECT id FROM space_area WHERE LOWER(name) = 'Fasilitas Publik'
                                                         AND client_address_id = :clientAddressId
                                                         AND deleted = FALSE LIMIT 1),
                     :clientAddressId, 'system', 0, NOW()
          ) AS tmp
          WHERE NOT EXISTS (
              SELECT 1 
              FROM space_area pc
              WHERE LOWER(pc.name) = LOWER(tmp.name)
                AND pc.client_address_id = :clientAddressId
                AND pc.deleted = FALSE       -- hanya kategori aktif yang dianggap exist
          );
            
    """, nativeQuery = true)
	void insertDefaultIdSubPublicArea(@Param("clientAddressId") Long clientAddressId);
}
