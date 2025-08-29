package com.divillafajar.app.pos.pos_app_sini.io.entity.client;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client_contact")
public class ClientContactEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5685114200320526829L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /*
    @Column(name = "pub_id", updatable = false, nullable = false)
    private String pubId;

     */

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "client_address_id")
    private ClientAddressEntity clientAddress;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_position")
    private String contactPosition;
}
