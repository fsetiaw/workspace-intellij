package com.divillafajar.app.pos.pos_app_sini.repo.auth;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthRepo extends CrudRepository<AuthorityEntity,String> {
    Optional<AuthorityEntity> findByAuthority(String auth);
    Optional<AuthorityEntity> findByUsername(String auth);
}
