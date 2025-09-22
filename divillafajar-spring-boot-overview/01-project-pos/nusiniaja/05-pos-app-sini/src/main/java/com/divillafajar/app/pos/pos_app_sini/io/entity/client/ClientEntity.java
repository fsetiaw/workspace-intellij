package com.divillafajar.app.pos.pos_app_sini.io.entity.client;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
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
    private Long id;



    @Column(name = "pub_id", updatable = false, nullable = false)
    private String pubId;

    @PrePersist
    public void generateId() {
        if (pubId == null) {
            pubId = UUID.randomUUID().toString(); // UUID jadi string
        }
    }


    @Column(name = "client_name", length = 50, nullable = false)
    private String clientName;

    @Lob
    @Column(name = "client_list_alias_name", nullable = true)
    private String clientListAliasName;

    @Column(name = "client_email", length = 99, nullable = false)
    private String clientEmail;

    @Column(name = "client_phone", length = 50, nullable = false)
    private String clientPhone;

    @Column(name = "client_type", length = 50, nullable = false)
    private String clientType;

    @Column(name = "deleted", nullable = true, columnDefinition = "TINYINT(1)")
    private Boolean deleted = false;
    /*
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH}, mappedBy = "clients")
    List<CustomerEntity> customers;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmploymentEntity> clients = new HashSet<>();
     */


    @Filters({
            @Filter(name = "deletedFilter", condition = "deleted = :isDeleted")
    })
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ClientAddressEntity> clientAddresses;

    @Column(name = "email_verification_status", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean email_verification_status = false;

    @Column(name = "email_verification_token", length = 255, nullable = true)
    private String email_verification_token;

    @Column(name = "email_verified_timestamp", nullable = true)
    private LocalDateTime email_verified_timestamp;

    @Column(name = "registration_timestamp", nullable = true)
    private LocalDateTime registration_timestamp;

    @Column(name = "registration_expiry_timestamp", nullable = true)
    private LocalDateTime registration_expiry_timestamp;
/*
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmploymentEntity> employments = new HashSet<>();

 */


}