package com.divillafajar.app.pos.pos_app_sini.io.entity.address;

import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AddressEntity")
@Table(
        name="address",
        uniqueConstraints = @UniqueConstraint(columnNames = {"addressName", "user"})
)
public class AddressEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7055023301565209276L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @Column(name = "address_name", nullable = false, length = 100)
    private String addressName;

    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "address_line1", nullable = false, length = 255)
    private String addressLine1;

    @Column(name = "address_line2", nullable = true, length = 255)
    private String addressLine2;

    @Column(name = "village", nullable = true, length = 100)
    private String village;

    @Column(name = "sub_district", nullable = true, length = 100)
    private String subDistrict;

    @Column(name = "city", nullable = true, length = 100)
    private String city;

    @Column(name = "province", nullable = false, length = 100)
    private String province;

    @Column(name = "postal_code", nullable = true, length = 20)
    private String postalCode;

    @Column(nullable = false, length = 100)
    private String country = "Indonesia";

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;


}
