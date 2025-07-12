package com.divillafajar.disiniaja.possini.entity.user.auth;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "authorities",
        uniqueConstraints = @UniqueConstraint(columnNames = {"username", "authority"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // untuk keperluan primary key entity ini

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String authority;
}
