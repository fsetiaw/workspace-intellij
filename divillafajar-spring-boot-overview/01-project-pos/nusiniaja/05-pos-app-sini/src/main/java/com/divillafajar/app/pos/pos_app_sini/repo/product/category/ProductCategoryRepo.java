package com.divillafajar.app.pos.pos_app_sini.repo.product.category;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepo extends CrudRepository<ProductCategoryEntity,Long> {
    Optional<ProductCategoryEntity> findByNameIgnoreCaseAndClientAddress_Id(String name, Long clientAddressId);

    @Query("""
        SELECT c FROM ProductCategoryEntity c
        WHERE c.clientAddress.id = :clientAddressId
        ORDER BY CASE WHEN c.parent IS NULL THEN 0 ELSE 1 END, c.parent.id, c.id
    """)
    List<ProductCategoryEntity> findAllByClientAddressSorted(@Param("clientAddressId") Long clientAddressId);

    @Query("""
        SELECT c
        FROM ProductCategoryEntity c
        WHERE c.clientAddress.id = :clientAddressId
        ORDER BY 
            CASE WHEN c.parent IS NULL THEN 0 ELSE 1 END,          
            LOWER(COALESCE(c.parent.name, c.name)),                
            CASE WHEN c.parent IS NULL THEN LOWER(c.name) ELSE LOWER(c.parent.name) END,
            LOWER(c.name)
        """)
    List<ProductCategoryEntity> findAllByClientAddressHierarchical(@Param("clientAddressId") Long clientAddressId);

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
}
