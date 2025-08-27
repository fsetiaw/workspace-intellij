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

    @Column(name = "address_name", length = 50, nullable = true)
    private String addressName;

    @Column(name = "address_line1", length = 250, nullable = true)
    private String addressLine1;

    @Column(name = "address_line2", length = 250, nullable = true)
    private String addressLine2;

    @Column(name = "village", length = 50, nullable = true)
    private String village;

    @Column(name = "sub_district", length = 50, nullable = true)
    private String subDistrict;

    @Column(name = "city", length = 50, nullable = true)
    private String city;

    @Column(name = "province", length = 50, nullable = true)
    private String province;

    @Column(name = "postal_code", length = 50, nullable = true)
    private String postalCode;

    @Column(name = "country", length = 50, nullable = true)
    private String country;
}
