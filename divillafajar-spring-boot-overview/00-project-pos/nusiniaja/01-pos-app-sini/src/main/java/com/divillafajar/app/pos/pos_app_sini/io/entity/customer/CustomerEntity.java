package com.divillafajar.app.pos.pos_app_sini.io.entity.customer;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
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
@Entity(name = "CustomerEntity")
@Table(name = "customer")
public class CustomerEntity implements Serializable {


    @Serial
    private static final long serialVersionUID = 2600997243162189476L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;


    @Column(name = "alias_name", nullable = false, length = 25)
    private String aliasName;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "client_customer",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private List<ClientEntity> clients;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private CustomerDetailsEntity customerDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
