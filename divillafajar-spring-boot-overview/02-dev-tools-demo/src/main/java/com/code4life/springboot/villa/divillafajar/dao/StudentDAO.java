package com.code4life.springboot.villa.divillafajar.dao;

import com.code4life.springboot.villa.divillafajar.entity.Student;

import java.util.List;

public interface StudentDAO {
    void save(Student newStudent);
    Student findById(Integer id);
    List<Student> findAll();

}
