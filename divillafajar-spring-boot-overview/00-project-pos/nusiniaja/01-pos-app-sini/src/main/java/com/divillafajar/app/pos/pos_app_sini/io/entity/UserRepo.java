package com.divillafajar.app.pos.pos_app_sini.io.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);
}
