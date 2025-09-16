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

    @Column(name = "client_email", length = 99, nullable = false)
    private String clientEmail;

    @Column(name = "client_phone", length = 50, nullable = false)
    private String clientPhone;

    @Column(name = "client_type", length = 50, nullable = false)
    private String clientType;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

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

/*
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmploymentEntity> employments = new HashSet<>();

 */


}