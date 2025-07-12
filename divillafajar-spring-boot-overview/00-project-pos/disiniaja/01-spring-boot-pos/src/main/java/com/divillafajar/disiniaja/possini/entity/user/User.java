package com.divillafajar.disiniaja.possini.entity.user;

import com.divillafajar.disiniaja.possini.entity.user.detail.UserDetailEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_detail_id")
    private UserDetailEntity userDetail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private UserEntity userAuthDetails;


}
