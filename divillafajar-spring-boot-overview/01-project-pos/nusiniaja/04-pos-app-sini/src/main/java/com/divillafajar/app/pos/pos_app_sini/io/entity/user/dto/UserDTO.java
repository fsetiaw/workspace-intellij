package com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmployeeEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO  {


    private Long id;
    private String pubId;
    private String emailVerificationToken;
    private boolean emailVerificationStatus;
    private String firstName;
    private String lastName;
    private String email;
    private String addressName;
    private String recipientName;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String village;
    private String subDistrict;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String username;
    private String password;
    private String authority;
    private String nik;
    private CustomerEntity customer;
    private EmployeeEntity employee;
    private List<AddressEntity> addresses;
    private String superKey;
}
