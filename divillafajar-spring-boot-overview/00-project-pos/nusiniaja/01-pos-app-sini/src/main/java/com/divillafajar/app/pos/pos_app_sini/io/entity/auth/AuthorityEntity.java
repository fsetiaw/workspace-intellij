package com.divillafajar.app.pos.pos_app_sini.io.entity.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity(name = "AuthorityEntity")
@Table(
        name = "authorities",
        uniqueConstraints = @UniqueConstraint(columnNames = {"username", "authority"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 2134251843498107435L;

    @Id
    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String authority;

    @OneToOne
    @MapsId
    @JoinColumn(name = "username")
    private NamePassEntity namePassEntity;

}
