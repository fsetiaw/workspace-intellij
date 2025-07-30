package com.divillafajar.app.pos.pos_app_sini.io.entity.customer;

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
@Table(name = "customer_details")
public class CustomerDetailsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1766262679106031273L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;




    @Column(name = "full_name", length = 99)
    private String fullName;
}
