package com.divillafajar.app.pos.pos_app_sini.io.entity.auth;

import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.GuestEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmployeeEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmploymentEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "NamePassEntity")
@Table(name = "users")
public class NamePassEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7312949496455092392L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @Column(length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "namePass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthorityEntity> authorities = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "guest_id")
    private GuestEntity guest;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "employment_id")
    private EmploymentEntity employment;

    /*
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "user_id")
    private UserEntity user;

     */


    /*


    @OneToOne(mappedBy = "userNamePass", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private AuthorityEntity userAuth;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authorities_id")
    private AuthorityEntity authDetail;

    @OneToOne(mappedBy = "userAuthDetails")
    private User user;

 */
}
