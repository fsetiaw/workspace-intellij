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
            CAST(CONCAT(name, '~', id) AS CHAR(1000)) AS full_path
          FROM product_category
          WHERE parent_id IS NULL
            AND client_address_id = :clientAddressId

          UNION ALL

          SELECT
            c.id,
            c.name,
            c.parent_id,
            c.client_address_id,
            CONCAT(cp.full_path, ' / ', c.name, '~', c.id) AS full_path
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

	@Query(value = """
        INSERT IGNORE INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
        VALUES
        ('Food', NULL, NULL, 1, 'system', 0),
        ('Beverage', NULL, NULL, 1, 'system', 0),
        ('Dessert', NULL, NULL, 1, 'system', 0),
        ('Package / Combo', NULL, NULL, 1, 'system', 0),
        ('Add-ons', NULL, NULL, 1, 'system', 0),
        ('Merchandise', NULL, NULL, 1, 'system', 0)
    """, nativeQuery = true)
	void insertDefaultEnCategories();

	@Query(value = """
       INSERT IGNORE INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
       VALUES
       ('Main Course', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Food' LIMIT 1) AS t), 1, 'system', 0),
       ('Appetizer', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Food' LIMIT 1) AS t), 1, 'system', 0),
       ('Side Dish', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Food' LIMIT 1) AS t), 1, 'system', 0),
       ('Snack', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Food' LIMIT 1) AS t), 1, 'system', 0),
       ('Soup', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Food' LIMIT 1) AS t), 1, 'system', 0),
       ('Salad', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Food' LIMIT 1) AS t), 1, 'system', 0);
    """, nativeQuery = true)
	void insertDefaultEnSubFoodCategories();

	@Query(value = """
       INSERT INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
       VALUES
	   ('Coffee', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Beverage' LIMIT 1) AS t), 1, 'system', 0),
	   ('Non-Coffee', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Beverage' LIMIT 1) AS t), 1, 'system', 0),
	   ('Tea', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Beverage' LIMIT 1) AS t), 1, 'system', 0),
	   ('Juice & Fresh Drink', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Beverage' LIMIT 1) AS t), 1, 'system', 0),
	   ('Mocktail', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Beverage' LIMIT 1) AS t), 1, 'system', 0),
	   ('Water', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Beverage' LIMIT 1) AS t), 1, 'system', 0);
    """, nativeQuery = true)
	void insertDefaultEnSubDrinkCategories();

/******************************ID***************************************/
	@Query(value = """
        INSERT IGNORE INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
        VALUES
        ('Makanan', NULL, NULL, 1, 'system', 0),
        ('Minuman', NULL, NULL, 1, 'system', 0),
        ('Pencuci Mulut', NULL, NULL, 1, 'system', 0),
        ('Paket / Combo', NULL, NULL, 1, 'system', 0),
        ('Tambahan', NULL, NULL, 1, 'system', 0),
        ('Merchandise', NULL, NULL, 1, 'system', 0);
    """, nativeQuery = true)
	void insertDefaultIdCategories();

	@Query(value = """
       INSERT IGNORE INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
       VALUES
       ('Hidangan Utama', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Makanan' LIMIT 1) AS t), 1, 'system', 0),
       ('Pembuka', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Makanan' LIMIT 1) AS t), 1, 'system', 0),
       ('Pendamping', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Makanan' LIMIT 1) AS t), 1, 'system', 0),
       ('Camilan', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Makanan' LIMIT 1) AS t), 1, 'system', 0),
       ('Sup', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Makanan' LIMIT 1) AS t), 1, 'system', 0),
       ('Salad', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Makanan' LIMIT 1) AS t), 1, 'system', 0);
    """, nativeQuery = true)
	void insertDefaultIdSubFoodCategories();

	@Query(value = """
      INSERT IGNORE INTO product_category (name, indent_level, parent_id, client_address_id, created_by, deleted)
      VALUES
      ('Kopi', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Minuman' LIMIT 1) AS t), 1, 'system', 0),
      ('Non-Kopi', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Minuman' LIMIT 1) AS t), 1, 'system', 0),
      ('Teh', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Minuman' LIMIT 1) AS t), 1, 'system', 0),
      ('Jus & Minuman Segar', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Minuman' LIMIT 1) AS t), 1, 'system', 0),
      ('Mocktail', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Minuman' LIMIT 1) AS t), 1, 'system', 0),
      ('Air Mineral', 1, (SELECT id FROM (SELECT id FROM product_category WHERE name='Minuman' LIMIT 1) AS t), 1, 'system', 0);
    """, nativeQuery = true)
	void insertDefaultIdSubDrinkCategories();
}
