package com.divillafajar.app.pos.pos_app_sini.repo.product.category;

import com.divillafajar.app.pos.pos_app_sini.io.projection.CategoryHierarchyProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductCategoryHierarchyProjection;
import com.divillafajar.app.pos.pos_app_sini.model.product.CategorySearchResultModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepo extends JpaRepository<ProductCategoryEntity,Long> {
    Optional<ProductCategoryEntity> findByNameIgnoreCaseAndClientAddress_Id(String name, Long clientAddressId);

	List<ProductCategoryEntity> findByParentId(Long id);

    @Query("""
        SELECT c FROM ProductCategoryEntity c
        WHERE c.clientAddress.id = :clientAddressId
        ORDER BY CASE WHEN c.parent IS NULL THEN 0 ELSE 1 END, c.parent.id, c.id
    """)
    List<ProductCategoryEntity> findAllByClientAddressSorted(@Param("clientAddressId") Long clientAddressId);

	@Query("""
	    SELECT EXISTS (
	        SELECT 1 FROM ProductCategoryEntity pc
	        WHERE pc.clientAddress.pubId = :pubId
	    )
	""")
	boolean isCategoryExistAtClientAddressByPubId(@Param("pubId") String pubId);

/*
    @Query(
            value = """
        WITH RECURSIVE category_hierarchy AS (
            SELECT 
                id,
                name,
                client_address_id,
                parent_id,
                LOWER(name) AS sort_key,
                0 AS level,
                CAST(LOWER(name) AS CHAR(255)) AS path
            FROM product_category
            WHERE client_address_id = :clientAddressId AND parent_id IS NULL

            UNION ALL

            SELECT 
                c.id,
                c.name,
                c.client_address_id,
                c.parent_id,
                LOWER(c.name) AS sort_key,
                ch.level + 1 AS level,
                CONCAT(ch.path, '>', LOWER(c.name)) AS path
            FROM product_category c
            INNER JOIN category_hierarchy ch ON c.parent_id = ch.id
        )
        SELECT 
            id,
            name,
            client_address_id,
            parent_id
        FROM category_hierarchy
        ORDER BY 
            SUBSTRING_INDEX(path, '>', 1),
            path
        """,
            nativeQuery = true
    )
    List<ProductCategoryEntity> findAllByClientAddressHierarchical(
            @Param("clientAddressId") Long clientAddressId);

 */
    @Query(
            value = """
                WITH RECURSIVE category_hierarchy AS (
                SELECT\s
                    id,
                    name,
                    client_address_id,
                    parent_id,
                    LOWER(name) AS sort_key,
                    0 AS indent_level,
                    CAST(LOWER(name) AS CHAR(255)) AS path
                FROM product_category
                WHERE client_address_id = :clientAddressId AND parent_id IS NULL
    
                UNION ALL
    
                SELECT\s
                    c.id,
                    c.name,
                    c.client_address_id,
                    c.parent_id,
                    LOWER(c.name) AS sort_key,
                    ch.indent_level + 1 AS indent_level,
                    CONCAT(ch.path, '>', LOWER(c.name)) AS path
                FROM product_category c
                INNER JOIN category_hierarchy ch ON c.parent_id = ch.id
            )
            SELECT\s
                id,
                name,
                client_address_id,
                parent_id,
                indent_level
            FROM category_hierarchy
            ORDER BY\s
                SUBSTRING_INDEX(path, '>', 1),
                path
            """,
            nativeQuery = true
    )
    List<ProductCategoryHierarchyProjection> findAllByClientAddressHierarchical(
            @Param("clientAddressId") Long clientAddressId);

    @Query("""
    SELECT DISTINCT c
    FROM ProductCategoryEntity c
    LEFT JOIN FETCH c.parent
    LEFT JOIN FETCH c.children
    WHERE c.clientAddress.id = :clientAddressId
    ORDER BY LOWER(c.name)
""")
    List<ProductCategoryEntity> findAllWithParentAndChildrenByClientAddress(
            @Param("clientAddressId") Long clientAddressId);

    @Query(value = """
        WITH RECURSIVE category_hierarchy AS (
          SELECT 
            id,
            name,
            parent_id,
            id AS top_parent_id,
            name AS top_parent_name,
            0 AS level
          FROM product_category
          WHERE parent_id IS NULL

          UNION ALL

          SELECT 
            c.id,
            c.name,
            c.parent_id,
            ch.top_parent_id,
            ch.top_parent_name,
            ch.level + 1 AS level
          FROM product_category c
          INNER JOIN category_hierarchy ch ON c.parent_id = ch.id
        )
        SELECT id, name, top_parent_id, top_parent_name, level
        FROM category_hierarchy
        WHERE id = :id
        """, nativeQuery = true)
    CategoryHierarchyProjectionDTO findCategoryHierarchyLevelById(@Param("id") Long id);

	@Query(value = """
        WITH RECURSIVE category_path AS (
            SELECT 
                id,
                name,
                client_address_id,
                parent_id,
                name AS path
            FROM product_category
            WHERE parent_id IS NULL

            UNION ALL

            SELECT 
                c.id,
                c.name,
                c.client_address_id,
                c.parent_id,
                CONCAT(cp.path, '>', c.name) AS path
            FROM product_category c
            INNER JOIN category_path cp ON c.parent_id = cp.id
        )
        SELECT 
            id,
            name,
            path
        FROM category_path
        WHERE client_address_id = :clientAddressId
          AND name LIKE CONCAT('%', :keyword, '%')
        """, nativeQuery = true)
	List<CategorySearchResultModel> searchCategoryWithPath(
			@Param("clientAddressId") Long clientAddressId,
			@Param("keyword") String keyword
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
          FROM product_category
          WHERE parent_id IS NULL
            AND client_address_id = :clientAddressId

          UNION ALL

          SELECT
            c.id,
            c.name,
            c.parent_id,
            c.client_address_id,
            CONCAT(cp.full_path, ' / ', REPLACE(c.name, '/', '|'), '~', c.id) AS full_path
          FROM product_category c
          INNER JOIN category_path cp ON cp.id = c.parent_id
        )

        SELECT
          cp.full_path
        FROM category_path cp
        WHERE NOT EXISTS (
          SELECT 1 FROM product_category child WHERE child.parent_id = cp.id
        )
        ORDER BY cp.full_path
        ;
    """,
            nativeQuery = true
    )
    List<String> findAllPathEndCategoryChildHierarchical(@Param("clientAddressId") Long clientAddressId);

    @Modifying
	@Query(value = """
         INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
       SELECT tmp.name, tmp.indent_level, tmp.parent_id, tmp.client_address_id, tmp.created_by, tmp.deleted
       FROM (
           SELECT 'Food' AS name, NULL AS indent_level, NULL AS parent_id, :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
           UNION ALL SELECT 'Beverage', NULL, NULL, :clientAddressId, 'system', 0
           UNION ALL SELECT 'Package / Combo', NULL, NULL, :clientAddressId, 'system', 0
           UNION ALL SELECT 'Add-ons', NULL, NULL, :clientAddressId, 'system', 0
           UNION ALL SELECT 'Merchandise', NULL, NULL, :clientAddressId, 'system', 0
       ) AS tmp
       WHERE NOT EXISTS (
           SELECT 1 FROM product_category pc
           WHERE LOWER(pc.name) = LOWER(tmp.name)
           AND pc.client_address_id = :clientAddressId
       );
    """, nativeQuery = true)
	void insertDefaultEnCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
	@Query(value = """
       INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
       SELECT * FROM (
           SELECT 'Snack' AS name, 1 AS indent_level,
                  (SELECT id FROM product_category WHERE LOWER(name) = 'food' AND client_address_id = :clientAddressId LIMIT 1) AS parent_id,
                  :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
           UNION ALL
           SELECT 'Soup & Salad', 1,
                  (SELECT id FROM product_category WHERE LOWER(name) = 'food' AND client_address_id = :clientAddressId LIMIT 1),
                  :clientAddressId, 'system', 0
           UNION ALL
           SELECT 'Main Course', 1,
                  (SELECT id FROM product_category WHERE LOWER(name) = 'food' AND client_address_id = :clientAddressId LIMIT 1),
                  :clientAddressId, 'system', 0
           UNION ALL
           SELECT 'Side Dish', 1,
                  (SELECT id FROM product_category WHERE LOWER(name) = 'food' AND client_address_id = :clientAddressId LIMIT 1),
                  :clientAddressId, 'system', 0
           UNION ALL
           SELECT 'Dessert', 1,
                  (SELECT id FROM product_category WHERE LOWER(name) = 'food' AND client_address_id = :clientAddressId LIMIT 1),
                  :clientAddressId, 'system', 0
       ) AS tmp
       WHERE NOT EXISTS (
           SELECT 1 FROM product_category pc
           WHERE LOWER(pc.name) = LOWER(tmp.name)
           AND pc.client_address_id = :clientAddressId
       )
    """, nativeQuery = true)
	void insertDefaultEnSubFoodCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
	@Query(value = """
       INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
          SELECT * FROM (
              SELECT 'Coffee' AS name, 1 AS indent_level,\s
                     (SELECT id FROM product_category WHERE LOWER(name) = 'beverage' AND client_address_id = :clientAddressId LIMIT 1) AS parent_id,
                     :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
              UNION ALL
              SELECT 'Non-Coffee', 1,\s
                     (SELECT id FROM product_category WHERE LOWER(name) = 'beverage' AND client_address_id = :clientAddressId LIMIT 1),
                     :clientAddressId, 'system', 0
              UNION ALL
              SELECT 'Tea', 1,\s
                     (SELECT id FROM product_category WHERE LOWER(name) = 'beverage' AND client_address_id = :clientAddressId LIMIT 1),
                     :clientAddressId, 'system', 0
              UNION ALL
              SELECT 'Juice & Fresh Drink', 1,\s
                     (SELECT id FROM product_category WHERE LOWER(name) = 'beverage' AND client_address_id = :clientAddressId LIMIT 1),
                     :clientAddressId, 'system', 0
              UNION ALL
              SELECT 'Mocktail', 1,\s
                     (SELECT id FROM product_category WHERE LOWER(name) = 'beverage' AND client_address_id = :clientAddressId LIMIT 1),
                     :clientAddressId, 'system', 0
              UNION ALL
              SELECT 'Water', 1,\s
                     (SELECT id FROM product_category WHERE LOWER(name) = 'beverage' AND client_address_id = :clientAddressId LIMIT 1),
                     :clientAddressId, 'system', 0
          ) AS tmp
          WHERE NOT EXISTS (
              SELECT 1 FROM product_category pc
              WHERE LOWER(pc.name) = LOWER(tmp.name)
              AND pc.client_address_id = :clientAddressId
          );
    """, nativeQuery = true)
	void insertDefaultEnSubBeverageCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
    @Query(value = """
         INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
         SELECT * FROM (
             SELECT 'Cake & Pastry' AS name, 2 AS indent_level,
                    (SELECT id FROM product_category WHERE LOWER(name) = 'Dessert' AND client_address_id = :clientAddressId LIMIT 1) AS parent_id,
                    :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
             UNION ALL
             SELECT 'Cold Dessert', 2,
                    (SELECT id FROM product_category WHERE LOWER(name) = 'Dessert' AND client_address_id = :clientAddressId LIMIT 1),
                    :clientAddressId, 'system', 0
             UNION ALL
             SELECT 'Traditional Dessert', 2,
                    (SELECT id FROM product_category WHERE LOWER(name) = 'Dessert' AND client_address_id = :clientAddressId LIMIT 1),
                    :clientAddressId, 'system', 0
         ) AS tmp
         WHERE NOT EXISTS (
             SELECT 1 FROM product_category pc
             WHERE LOWER(pc.name) = LOWER(tmp.name)
             AND pc.client_address_id = :clientAddressId
         )
    """, nativeQuery = true)
    void insertDefaultEnSubDessertCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
    @Query(value = """
        INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
        SELECT * FROM (
            SELECT 'Kids Meal' AS name, 1 AS indent_level,
                   (SELECT id FROM product_category
                    WHERE LOWER(name) = LOWER('Package / Combo')
                    AND client_address_id = :clientAddressId
                    LIMIT 1) AS parent_id,
                   :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
            UNION ALL
            SELECT 'Couple Set', 1,
                   (SELECT id FROM product_category
                    WHERE LOWER(name) = LOWER('Package / Combo')
                    AND client_address_id = :clientAddressId
                    LIMIT 1),
                   :clientAddressId, 'system', 0
            UNION ALL
            SELECT 'Family Set', 1,
                   (SELECT id FROM product_category
                    WHERE LOWER(name) = LOWER('Package / Combo')
                    AND client_address_id = :clientAddressId
                    LIMIT 1),
                   :clientAddressId, 'system', 0
            UNION ALL
            SELECT 'Promo Bundle', 1,
                   (SELECT id FROM product_category
                    WHERE LOWER(name) = LOWER('Package / Combo')
                    AND client_address_id = :clientAddressId
                    LIMIT 1),
                   :clientAddressId, 'system', 0
        ) AS tmp
        WHERE NOT EXISTS (
            SELECT 1 FROM product_category pc
            WHERE LOWER(pc.name) = LOWER(tmp.name)
            AND pc.client_address_id = :clientAddressId
        )
    """, nativeQuery = true)
    void insertDefaultEnSubComboCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
    @Query(value = """
      INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
      SELECT * FROM (
          SELECT 'Topping' AS name, 1 AS indent_level,
                 (SELECT id FROM product_category
                  WHERE LOWER(name) = LOWER('Add-ons')
                  AND client_address_id = :clientAddressId
                  LIMIT 1) AS parent_id,
                 :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
          UNION ALL
          SELECT 'Sauce', 1,
                 (SELECT id FROM product_category
                  WHERE LOWER(name) = LOWER('Add-ons')
                  AND client_address_id = :clientAddressId
                  LIMIT 1),
                 :clientAddressId, 'system', 0
          UNION ALL
          SELECT 'Extra Portion', 1,
                 (SELECT id FROM product_category
                  WHERE LOWER(name) = LOWER('Add-ons')
                  AND client_address_id = :clientAddressId
                  LIMIT 1),
                 :clientAddressId, 'system', 0
      ) AS tmp
      WHERE NOT EXISTS (
          SELECT 1 FROM product_category pc
          WHERE LOWER(pc.name) = LOWER(tmp.name)
          AND pc.client_address_id = :clientAddressId
      )
    """, nativeQuery = true)
    void insertDefaultEnSubAddOnsCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
    @Query(value = """
       INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
       SELECT * FROM (
           SELECT 'Souvenir' AS name, 1 AS indent_level,
                  (SELECT id FROM product_category
                   WHERE LOWER(name) = LOWER('Merchandise')
                   AND client_address_id = :clientAddressId
                   LIMIT 1) AS parent_id,
                  :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
           UNION ALL
           SELECT 'Packaged Product', 1,
                  (SELECT id FROM product_category
                   WHERE LOWER(name) = LOWER('Merchandise')
                   AND client_address_id = :clientAddressId
                   LIMIT 1),
                  :clientAddressId, 'system', 0
       ) AS tmp
       WHERE NOT EXISTS (
           SELECT 1 FROM product_category pc
           WHERE LOWER(pc.name) = LOWER(tmp.name)
           AND pc.client_address_id = :clientAddressId
       )
    """, nativeQuery = true)
    void insertDefaultEnSubMerchandiseCategories(@Param("clientAddressId") Long clientAddressId);

/******************************ID***************************************/
    @Modifying
	@Query(value = """
        INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
       SELECT tmp.name, tmp.indent_level, tmp.parent_id, tmp.client_address_id, tmp.created_by, tmp.deleted
       FROM (
           SELECT 'Makanan' AS name, NULL AS indent_level, NULL AS parent_id, :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
           UNION ALL SELECT 'Minuman', NULL, NULL, :clientAddressId, 'system', 0
           UNION ALL SELECT 'Paket / Combo', NULL, NULL, :clientAddressId, 'system', 0
           UNION ALL SELECT 'Tambahan', NULL, NULL, :clientAddressId, 'system', 0
           UNION ALL SELECT 'Oleh-2', NULL, NULL, :clientAddressId, 'system', 0
       ) AS tmp
       WHERE NOT EXISTS (
           SELECT 1 FROM product_category pc
           WHERE LOWER(pc.name) = LOWER(tmp.name)
           AND pc.client_address_id = :clientAddressId
       );
    """, nativeQuery = true)
	void insertDefaultIdCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
	@Query(value = """
      INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
      SELECT * FROM (
          SELECT 'Camilan' AS name, 1 AS indent_level,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'makanan' AND client_address_id = :clientAddressId LIMIT 1) AS parent_id,
                 :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
          UNION ALL
          SELECT 'Sup & Salad', 1,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'makanan' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
          UNION ALL
          SELECT 'Hidangan Utama', 1,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'makanan' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
          UNION ALL
          SELECT 'Pendamping', 1,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'makanan' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
          UNION ALL
          SELECT 'Penutup', 1,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'makanan' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
      ) AS tmp
      WHERE NOT EXISTS (
          SELECT 1 FROM product_category pc
          WHERE LOWER(pc.name) = LOWER(tmp.name)
          AND pc.client_address_id = :clientAddressId
      )
    """, nativeQuery = true)
	void insertDefaultIdSubFoodCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
	@Query(value = """
      INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
      SELECT * FROM (
          SELECT 'Kopi' AS name, 1 AS indent_level,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'Minuman' AND client_address_id = :clientAddressId LIMIT 1) AS parent_id,
                 :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
          UNION ALL
          SELECT 'Non-Kopi', 1,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'Minuman' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
          UNION ALL
          SELECT 'Teh', 1,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'Minuman' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
          UNION ALL
          SELECT 'Jus & Minuman Segar', 1,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'Minuman' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
          UNION ALL
          SELECT 'Mocktail', 1,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'Minuman' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
          UNION ALL
          SELECT 'Air Mineral', 1,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'Minuman' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
      ) AS tmp
      WHERE NOT EXISTS (
          SELECT 1 FROM product_category pc
          WHERE LOWER(pc.name) = LOWER(tmp.name)
          AND pc.client_address_id = :clientAddressId
      );
    """, nativeQuery = true)
	void insertDefaultIdSubBeverageCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
    @Query(value = """
      INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
      SELECT * FROM (
          SELECT 'Kue & Pastry' AS name, 2 AS indent_level,\s
                 (SELECT id FROM product_category WHERE LOWER(name) = 'penutup' AND client_address_id = :clientAddressId LIMIT 1) AS parent_id,
                 :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
          UNION ALL
          SELECT 'Dingin / Es', 2,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'penutup' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
          UNION ALL
          SELECT 'Tradisional', 2,
                 (SELECT id FROM product_category WHERE LOWER(name) = 'penutup' AND client_address_id = :clientAddressId LIMIT 1),
                 :clientAddressId, 'system', 0
      ) AS tmp
      WHERE NOT EXISTS (
          SELECT 1 FROM product_category pc
          WHERE LOWER(pc.name) = LOWER(tmp.name)
          AND pc.client_address_id = :clientAddressId
      )
    """, nativeQuery = true)
    void insertDefaultIdSubDessertCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
    @Query(value = """
          INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
          SELECT * FROM (
              SELECT 'Makanan Anak' AS name, 1 AS indent_level,
                     (SELECT id FROM product_category
                      WHERE LOWER(name) = LOWER('Paket / Combo')
                      AND client_address_id = :clientAddressId
                      LIMIT 1) AS parent_id,
                     :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
              UNION ALL
              SELECT 'Paket Berdua', 1,
                     (SELECT id FROM product_category
                      WHERE LOWER(name) = LOWER('Paket / Combo')
                      AND client_address_id = :clientAddressId
                      LIMIT 1),
                     :clientAddressId, 'system', 0
              UNION ALL
              SELECT 'Paket Keluarga', 1,
                     (SELECT id FROM product_category
                      WHERE LOWER(name) = LOWER('Paket / Combo')
                      AND client_address_id = :clientAddressId
                      LIMIT 1),
                     :clientAddressId, 'system', 0
              UNION ALL
              SELECT 'Paket Promo', 1,
                     (SELECT id FROM product_category
                      WHERE LOWER(name) = LOWER('Paket / Combo')
                      AND client_address_id = :clientAddressId
                      LIMIT 1),
                     :clientAddressId, 'system', 0
          ) AS tmp
          WHERE NOT EXISTS (
              SELECT 1 FROM product_category pc
              WHERE LOWER(pc.name) = LOWER(tmp.name)
              AND pc.client_address_id = :clientAddressId
          )
    """, nativeQuery = true)
    void insertDefaultIdSubComboCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
    @Query(value = """
               INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
      SELECT * FROM (
          SELECT 'Topping' AS name, 1 AS indent_level,
                 (SELECT id FROM product_category
                  WHERE LOWER(name) = LOWER('tambahan')
                  AND client_address_id = :clientAddressId
                  LIMIT 1) AS parent_id,
                 :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
          UNION ALL
          SELECT 'Saus', 1,
                 (SELECT id FROM product_category
                  WHERE LOWER(name) = LOWER('tambahan')
                  AND client_address_id = :clientAddressId
                  LIMIT 1),
                 :clientAddressId, 'system', 0
          UNION ALL
          SELECT 'Porsi Tambahan', 1,
                 (SELECT id FROM product_category
                  WHERE LOWER(name) = LOWER('tambahan')
                  AND client_address_id = :clientAddressId
                  LIMIT 1),
                 :clientAddressId, 'system', 0
      ) AS tmp
      WHERE NOT EXISTS (
          SELECT 1 FROM product_category pc
          WHERE LOWER(pc.name) = LOWER(tmp.name)
          AND pc.client_address_id = :clientAddressId
      )
    """, nativeQuery = true)
    void insertDefaultIdSubAddOnsCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
    @Query(value = """
       INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
       SELECT * FROM (
           SELECT 'Souvenir' AS name, 1 AS indent_level,
                  (SELECT id FROM product_category
                   WHERE LOWER(name) = LOWER('Oleh-2')
                   AND client_address_id = :clientAddressId
                   LIMIT 1) AS parent_id,
                  :clientAddressId AS client_address_id, 'system' AS created_by, 0 AS deleted
           UNION ALL
           SELECT 'Produk Kemasan', 1,
                  (SELECT id FROM product_category
                   WHERE LOWER(name) = LOWER('Oleh-2')
                   AND client_address_id = :clientAddressId
                   LIMIT 1),
                  :clientAddressId, 'system', 0
       ) AS tmp
       WHERE NOT EXISTS (
           SELECT 1 FROM product_category pc
           WHERE LOWER(pc.name) = LOWER(tmp.name)
           AND pc.client_address_id = :clientAddressId
       )
    """, nativeQuery = true)
    void insertDefaultIdSubMerchandiseCategories(@Param("clientAddressId") Long clientAddressId);

    @Modifying
    @Query("DELETE FROM ProductCategoryEntity c WHERE c.clientAddress.id = :clientAddressId")
    void deleteAllCategoriesByClientAddressId(@Param("clientAddressId") Long clientAddressId);
}
