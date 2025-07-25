package com.divillafajar.app.pos.pos_app_sini.io.entity.user;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="UserEntity")
@Table(name="user")
public class UserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1169334999915236294L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private NamePassEntity userAuthDetails;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CustomerEntity customer;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<AddressEntity> addresses;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "email", nullable = true)
    private String email;

}
