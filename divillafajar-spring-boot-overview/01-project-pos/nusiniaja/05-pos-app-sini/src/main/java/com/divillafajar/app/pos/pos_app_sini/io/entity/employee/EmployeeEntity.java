package com.divillafajar.app.pos.pos_app_sini.io.entity.employee;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.model.ContactPerson;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "EmployeeEntity")
@Table(name = "employee")
public class EmployeeEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5129767027009165514L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_emergency_contacts", joinColumns = @JoinColumn(name = "employee_id"))
    private List<ContactPerson> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<EmploymentEntity> employments = new HashSet<>();

    /*
    @OneToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private UserEntity user;

     */
// === 🔹 Metadata ===
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "updated_by", length = 100)
	private String updatedBy;

	@Column(name = "deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean deleted = false;



}
