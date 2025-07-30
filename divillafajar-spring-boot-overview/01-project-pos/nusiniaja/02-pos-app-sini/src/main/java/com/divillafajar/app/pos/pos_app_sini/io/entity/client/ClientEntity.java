package com.divillafajar.app.pos.pos_app_sini.io.entity.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ClientEntity")
@Table(name = "client")
public class ClientEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -4001758103879878753L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "client_name", length = 50, nullable = false)
    private String clientName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH}, mappedBy = "clients")
    List<CustomerEntity> customers;



    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private ClientDetailsEntity clientDetails;
}