package com.divillafajar.disiniaja.possini.entity.user.address;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="address")
public class AddressEntity {


    private int id;

    @Column(nullable = false, length = 100)
    private String address_name;
    @Column(nullable = false, length = 100)
    private String recipient_name;
    @Column(nullable = false, length = 20)
    private String phone_number;
    @Column(nullable = false, length = 255)
    private String address_line1;
    @Column(nullable = true, length = 255)
    private String address_line2;
    @Column(nullable = true, length = 100)
    private String village;
    @Column(nullable = true, length = 100)
    private String sub_district;
    @Column(nullable = false, length = 100)
    private String city;
    @Column(nullable = false, length = 100)
    private String province;
    @Column(nullable = true, length = 20)
    private String postal_code;
    @Column(nullable = false, length = 100)
    private String country = "Indonesia";
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
