package com.divillafajar.app.pos.pos_app_sini.repo.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.space.unit.AreaUnitEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitAreaRepo extends CrudRepository<AreaUnitEntity, Long> {
	AreaUnitEntity findByName(String name);
	boolean existsByName(String name);
}
