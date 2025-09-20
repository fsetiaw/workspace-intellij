package com.divillafajar.app.pos.pos_app_sini.repo.auth;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthRepo extends CrudRepository<AuthorityEntity,String> {
}
