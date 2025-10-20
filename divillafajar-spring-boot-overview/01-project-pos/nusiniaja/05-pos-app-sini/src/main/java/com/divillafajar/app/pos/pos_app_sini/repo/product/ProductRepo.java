package com.divillafajar.app.pos.pos_app_sini.repo.product;

import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductWithCategoryPathDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<ProductEntity,Long> {
	@Query(value = """
    SELECT p.* 
    FROM product p
    JOIN client_address ca ON p.client_address_id = ca.id
    WHERE ca.pub_id = :pubId
      AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
      AND p.deleted = FALSE
    LIMIT 1
""", nativeQuery = true)
	ProductEntity searchProductsByClientAddressPubIdAndName(
			@Param("pubId") String pubId,
			@Param("name") String name
	);

	@Query(value = """
        WITH RECURSIVE category_hierarchy AS (
            SELECT 
                c.id,
                c.name,
                c.parent_id,
                c.id AS root_id,
                c.name AS root_name,
                c.name AS category_path
            FROM product_category c
            WHERE c.id = :categoryId

            UNION ALL

            SELECT 
                child.id,
                child.name,
                child.parent_id,
                ch.root_id,
                ch.root_name,
                CONCAT(ch.category_path, ' / ', child.name) AS category_path
            FROM product_category child
            INNER JOIN category_hierarchy ch ON child.parent_id = ch.id
        )
        SELECT 
            p.id AS id,
            p.name AS name,
            p.description AS description,
            p.thumbnail_url as thumbnail_url,
            p.image_url as image_url,
            p.status AS status,
            pc.name AS category_name,
            ch.category_path AS category_path,
            ch.root_name AS top_category_name
        FROM product p
        JOIN product_category pc ON p.category_id = pc.id
        JOIN category_hierarchy ch ON pc.id = ch.id
        JOIN client_address ca ON p.client_address_id = ca.id
        WHERE ca.pub_id = :pubId
          AND p.deleted = FALSE
        ORDER BY 
            LOWER(ch.root_name) ASC,
            LOWER(ch.category_path) ASC,
            LOWER(p.name) ASC
        """, nativeQuery = true)
	List<ProductWithCategoryPathDTO> findProductsWithCategoryPathByClientAndCategory(
			@Param("pubId") String pubId,
			@Param("categoryId") Long categoryId
	);

    @Query(value = """
    WITH RECURSIVE category_hierarchy AS (
        SELECT 
            c.id,
            c.name,
            c.parent_id,
            c.id AS root_id,
            c.name AS root_name,
            c.name AS category_path
        FROM product_category c
        WHERE c.id = :categoryId

        UNION ALL

        SELECT 
            child.id,
            child.name,
            child.parent_id,
            ch.root_id,
            ch.root_name,
            CONCAT(ch.category_path, ' / ', child.name) AS category_path
        FROM product_category child
        INNER JOIN category_hierarchy ch ON child.parent_id = ch.id
    )
    SELECT 
        p.id AS id,
        p.name AS name,
        p.description AS description,
        p.thumbnail_url as thumbnail_url,
        p.image_url as image_url,
        p.status AS status,
        pc.name AS category_name,
        ch.category_path AS category_path,
        ch.root_name AS top_category_name
    FROM product p
    JOIN product_category pc ON p.category_id = pc.id
    JOIN category_hierarchy ch ON pc.id = ch.id
    JOIN client_address ca ON p.client_address_id = ca.id
    WHERE ca.pub_id = :pubId
      AND p.deleted = FALSE
      AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    ORDER BY 
        LOWER(ch.root_name) ASC,
        LOWER(ch.category_path) ASC,
        LOWER(p.name) ASC
    """, nativeQuery = true)
    List<ProductWithCategoryPathDTO> findProductsWithCategoryPathByClientAddressAndCategoryFilterByKwordProductName(
            @Param("pubId") String pubId,
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword
    );

}
