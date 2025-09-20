package com.divillafajar.app.pos.pos_app_sini.repo.employee;

import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmployeeEntity;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepo extends CrudRepository<EmployeeEntity,Long> {
}
