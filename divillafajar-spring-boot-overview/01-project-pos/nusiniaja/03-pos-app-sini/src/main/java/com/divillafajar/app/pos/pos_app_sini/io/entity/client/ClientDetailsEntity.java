package com.divillafajar.app.pos.pos_app_sini.io.entity.client;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client_details")
public class ClientDetailsEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 8882992899767078972L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "client_id")
    private ClientEntity client;
}
