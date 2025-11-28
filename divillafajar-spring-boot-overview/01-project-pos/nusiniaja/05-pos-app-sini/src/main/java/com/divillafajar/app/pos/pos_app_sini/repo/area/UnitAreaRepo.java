package com.divillafajar.app.pos.pos_app_sini.repo.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.unit.AreaUnitEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitAreaRepo extends CrudRepository<AreaUnitEntity, Long> {
	AreaUnitEntity findByName(String name);
	boolean existsByName(String name);

	@Query(
			value = "SELECT * FROM area_unit " +
					"WHERE client_address_id = :clientAddressId " +
					"AND space_area_id = :spaceAreaId " +
					"AND deleted = 0",
			nativeQuery = true
	)
	List<AreaUnitEntity> findUnitAreaByClientAndAreaId(
			@Param("clientAddressId") Long clientAddressId,
			@Param("spaceAreaId") Long spaceAreaId
	);
}
