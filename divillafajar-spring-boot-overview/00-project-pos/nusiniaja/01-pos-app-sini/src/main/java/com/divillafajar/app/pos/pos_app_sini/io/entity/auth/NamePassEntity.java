package com.divillafajar.app.pos.pos_app_sini.io.entity.auth;

import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "NamePassEntity")
@Table(name = "users")
public class NamePassEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7312949496455092392L;

    @Id
    @Column(length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @OneToOne(mappedBy = "userNamePass", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private AuthorityEntity userAuth;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "user_id")
    private UserEntity user;
/*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authorities_id")
    private AuthorityEntity authDetail;

    @OneToOne(mappedBy = "userAuthDetails")
    private User user;

 */
}
