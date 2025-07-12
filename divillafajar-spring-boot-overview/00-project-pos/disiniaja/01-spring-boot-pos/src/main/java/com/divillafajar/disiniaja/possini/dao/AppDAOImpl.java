package com.divillafajar.disiniaja.possini.dao;

import com.divillafajar.disiniaja.possini.entity.user.User;
import com.divillafajar.disiniaja.possini.entity.user.detail.UserDetailEntity;
import com.divillafajar.disiniaja.possini.entity.user.UserEntity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AppDAOImpl implements AppDAO {

    // define field from entiy manager
    private EntityManager entityManager;

    // ineject entity manager
    @Autowired
    public AppDAOImpl(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    @Transactional
    public void save(UserEntity user) {
        entityManager.persist(user);
    }

    @Override
    public UserDetailEntity findUserDetailById(int id) {
        return entityManager.find(UserDetailEntity.class, id);
    }


}
