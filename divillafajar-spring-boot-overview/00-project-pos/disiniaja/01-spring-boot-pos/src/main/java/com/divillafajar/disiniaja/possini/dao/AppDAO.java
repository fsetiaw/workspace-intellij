package com.divillafajar.disiniaja.possini.dao;

import com.divillafajar.disiniaja.possini.entity.user.User;
import com.divillafajar.disiniaja.possini.entity.user.address.AddressEntity;
import com.divillafajar.disiniaja.possini.entity.user.detail.UserDetailEntity;
import com.divillafajar.disiniaja.possini.entity.user.UserEntity;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AppDAO {

    User save(User user);
    UserDetailEntity save(UserDetailEntity userDetail);
    void save(UserEntity user);
    UserDetailEntity findUserDetailById(int id);
    void save(List<AddressEntity> addressEntities, int userId);
}
