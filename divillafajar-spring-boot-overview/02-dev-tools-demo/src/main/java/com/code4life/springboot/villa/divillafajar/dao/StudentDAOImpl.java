package com.code4life.springboot.villa.divillafajar.dao;

import com.code4life.springboot.villa.divillafajar.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class StudentDAOImpl implements StudentDAO {
    //define field for entityManager
    private EntityManager entityManager;

    //inject EM
    @Autowired
    public StudentDAOImpl(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

    //implement save method
    @Override
    @Transactional
    public void save(Student newStudent) {
        entityManager.persist(newStudent);
    }

    @Override
    public Student findById(Integer id) {
        Student stu = entityManager.find(Student.class, id);
        System.out.println("stu name="+stu.getEmail());
        return stu;
    }

    @Override
    public List<Student> findAll() {
        TypedQuery<Student> theQuery = entityManager.createQuery("From Student order by lastName", Student.class);
        return theQuery.getResultList();
    }
}
