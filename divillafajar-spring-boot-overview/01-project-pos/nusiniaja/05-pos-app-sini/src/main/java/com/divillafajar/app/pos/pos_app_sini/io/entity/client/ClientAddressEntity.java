package com.divillafajar.app.pos.pos_app_sini.io.entity.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.ClientAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmploymentEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.scope.ScopeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client_address")
public class ClientAddressEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 8882992899767078972L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @Column(name = "pub_id", updatable = false, nullable = false)
    private String pubId;

    @PrePersist
    public void generateId() {
        if (active == null) {
            active = true;
        }
        if(usedDefaultCategory==null)
            usedDefaultCategory=false;

        if(deleted==null)
            deleted=false;

        if (pubId == null) {
            pubId = UUID.randomUUID().toString(); // UUID jadi string
        }
    }

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @OneToMany(mappedBy = "clientAddress", cascade = CascadeType.ALL)
    private List<ClientContactEntity> contacts;

    @OneToMany(mappedBy = "clientAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmploymentEntity> employments = new HashSet<>();

    @OneToMany(mappedBy = "clientAddress", cascade = CascadeType.ALL)
    private List<ClientAreaEntity> areas;

    @OneToMany(mappedBy = "clientAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ScopeEntity> scopes = new HashSet<>();

    @OneToMany(mappedBy = "clientAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategoryEntity> categories = new ArrayList<>();


    @Column(name = "address_name", length = 99, nullable = true)
    private String addressName;

    @Column(name = "address_nickname", length = 99, nullable = true)
    private String addressNickname;

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

    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean active = true;

    @Column(name = "phone", nullable = true, length = 20)
    private String phone;

    @Column(name = "email", length = 50, nullable = true)
    private String email;

    @Column(name = "location_category", length = 50, nullable = false)
    private String locationCategory;

    @Column(name = "guest_capacity", length = 20, nullable = false)
    private String guestCapacity;

	// === 🔹 Metadata ===
	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "updated_by", length = 100)
	private String updatedBy;

	@Column(name = "deleted", nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean deleted = false;

    @Column(name = "used_default_category", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean usedDefaultCategory = false;
}
