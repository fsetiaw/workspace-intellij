package com.divillafajar.app.pos.pos_app_sini.io.entity.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
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

        if(deleted==null)
            deleted=false;
    }

    @Column(name = "client_name", length = 50, nullable = false)
    private String clientName;

    //@Column(name = "client_name", length = 50, nullable = false)
    //private String clientBusinessField;

    @Lob
    @Column(name = "client_list_alias_name", nullable = true)
    private String clientListAliasName;

    @Column(name = "client_email", length = 99, nullable = true)
    private String clientEmail;

    @Column(name = "client_phone", length = 50, nullable = true)
    private String clientPhone;

    @Column(name = "client_type", length = 50, nullable = false)
    private String clientType;

    @Column(name = "deleted", nullable = true, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean deleted = false;

    //@Filters({
    //        @Filter(name = "deletedFilter", condition = "deleted = :isDeleted")
    //})
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ClientAddressEntity> clientAddresses;


    // Relasi OneToOne ke NamePassEntity (child)
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private NamePassEntity namePass;

	// === 🔹 Metadata ===
	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "updated_by", length = 100)
	private String updatedBy;


    /*
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH}, mappedBy = "clients")
    List<CustomerEntity> customers;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmploymentEntity> clients = new HashSet<>();
     */
    /*
    //@Column(name = "email_verification_status", nullable = false, columnDefinition = "TINYINT(1)")
    //private Boolean email_verification_status = false;

    //@Column(name = "email_verification_token", length = 255, nullable = true)
    //private String email_verification_token;

    //@Column(name = "email_verified_timestamp", nullable = true)
    //private LocalDateTime email_verified_timestamp;

    //@Column(name = "registration_timestamp", nullable = true)
    //private LocalDateTime registration_timestamp;

    //@Column(name = "registration_expiry_timestamp", nullable = true)
    //private LocalDateTime registration_expiry_timestamp;

     */

/*
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmploymentEntity> employments = new HashSet<>();

 */


}