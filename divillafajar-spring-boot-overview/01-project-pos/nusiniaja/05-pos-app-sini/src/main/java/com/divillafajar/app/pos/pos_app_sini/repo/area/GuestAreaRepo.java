package com.divillafajar.app.pos.pos_app_sini.repo.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.space.GuestAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestAreaRepo extends JpaRepository<GuestAreaEntity, Long> {
	boolean existsByClientAddress_PubId(String pubId);

	@Query("""
        SELECT CASE WHEN COUNT(ga) > 0 THEN TRUE ELSE FALSE END
        FROM GuestAreaEntity ga
        JOIN ga.clientAddress ca
        WHERE LOWER(ga.name) = LOWER(:name)
          AND ca.pubId = :clientAddressPubId
          AND ga.deleted = false
    """)
	boolean existsByNameInGuestArea(@Param("clientAddressPubId") String clientAddressPubId,
	                                @Param("name") String name);
}
