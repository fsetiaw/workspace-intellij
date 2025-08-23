package com.divillafajar.app.pos.pos_app_sini.service.customer;

import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.dto.CustomerDTO;

public interface CustomerService {
    CustomerDTO loginCustomer(CustomerDTO customerDTO);
}
