package com.divillafajar.app.pos.pos_app_sini.repo.customer;

import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends CrudRepository<CustomerEntity, Long> {
    CustomerEntity findCustomerByAliasName(String alias);
}
