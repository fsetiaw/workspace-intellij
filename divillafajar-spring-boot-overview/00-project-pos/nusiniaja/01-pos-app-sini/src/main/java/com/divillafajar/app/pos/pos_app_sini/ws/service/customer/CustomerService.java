package com.divillafajar.app.pos.pos_app_sini.ws.service.customer;

import com.divillafajar.app.pos.pos_app_sini.ws.model.shared.dto.CustomerDTO;

public interface CustomerService {
    CustomerDTO loginCustomer(CustomerDTO customerDTO);
}
