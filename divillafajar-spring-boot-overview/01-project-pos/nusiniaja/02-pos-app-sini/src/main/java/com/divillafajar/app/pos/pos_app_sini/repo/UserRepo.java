package com.divillafajar.app.pos.pos_app_sini.repo;

import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);
    UserEntity findUserByCustomer(CustomerEntity customerEntity);
}
