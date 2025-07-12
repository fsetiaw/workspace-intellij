package com.divillafajar.disiniaja.possini.dao;

import com.divillafajar.disiniaja.possini.entity.user.User;
import com.divillafajar.disiniaja.possini.entity.user.detail.UserDetailEntity;
import com.divillafajar.disiniaja.possini.entity.user.UserEntity;

public interface AppDAO {

    void save(User user);
    void save(UserEntity user);
    UserDetailEntity findUserDetailById(int id);
}
