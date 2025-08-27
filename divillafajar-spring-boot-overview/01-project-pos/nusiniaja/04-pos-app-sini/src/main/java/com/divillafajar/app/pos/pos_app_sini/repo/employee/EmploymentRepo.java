package com.divillafajar.app.pos.pos_app_sini.repo.employee;

import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmploymentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentRepo extends CrudRepository<EmploymentEntity,Long> {
}
