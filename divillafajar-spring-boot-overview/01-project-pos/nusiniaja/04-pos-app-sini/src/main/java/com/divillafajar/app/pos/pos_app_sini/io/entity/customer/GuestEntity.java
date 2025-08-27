package com.divillafajar.app.pos.pos_app_sini.io.entity.customer;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "GuestEntity")
@Table(name = "guest")
public class GuestEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2685398311222829268L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pub_id", length = 30, nullable = false)
    private String pubId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @OneToOne(mappedBy = "guest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NamePassEntity userAuthDetails;

}
