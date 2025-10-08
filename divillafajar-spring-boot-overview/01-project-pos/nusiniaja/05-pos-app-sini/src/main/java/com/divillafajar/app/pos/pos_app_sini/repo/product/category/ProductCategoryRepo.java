package com.divillafajar.app.pos.pos_app_sini.repo.product.category;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.CategoryHierarchyProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepo extends CrudRepository<ProductCategoryEntity,Long> {
    Optional<ProductCategoryEntity> findByNameIgnoreCaseAndClientAddress_Id(String name, Long clientAddressId);

	List<ProductCategoryEntity> findByParentId(Long id);

    @Query("""
        SELECT c FROM ProductCategoryEntity c
        WHERE c.clientAddress.id = :clientAddressId
        ORDER BY CASE WHEN c.parent IS NULL THEN 0 ELSE 1 END, c.parent.id, c.id
    """)
    List<ProductCategoryEntity> findAllByClientAddressSorted(@Param("clientAddressId") Long clientAddressId);
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
                WHERE client_address_id = 1 AND parent_id IS NULL
    
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
    List<ProductCategoryEntity> findAllByClientAddressHierarchical(
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
}
