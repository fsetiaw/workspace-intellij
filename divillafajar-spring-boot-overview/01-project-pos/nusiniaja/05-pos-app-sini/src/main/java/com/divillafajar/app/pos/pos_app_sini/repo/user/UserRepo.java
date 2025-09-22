package com.divillafajar.app.pos.pos_app_sini.repo.user;

import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    //UserEntity findUserByCustomer(CustomerEntity customerEntity);
    UserEntity findByEmailAndPhone(String email, String phone);
    UserEntity findByEmailOrPhone(String email, String phone);
}
