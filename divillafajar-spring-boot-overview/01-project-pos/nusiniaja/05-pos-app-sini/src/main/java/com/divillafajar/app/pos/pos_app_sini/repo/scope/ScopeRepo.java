package com.divillafajar.app.pos.pos_app_sini.repo.scope;

import com.divillafajar.app.pos.pos_app_sini.io.entity.scope.ScopeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScopeRepo extends CrudRepository<ScopeEntity,Long> {

}
