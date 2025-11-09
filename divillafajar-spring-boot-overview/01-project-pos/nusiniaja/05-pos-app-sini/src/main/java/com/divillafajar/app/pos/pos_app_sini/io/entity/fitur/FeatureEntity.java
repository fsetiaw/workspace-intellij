package com.divillafajar.app.pos.pos_app_sini.io.entity.fitur;

import com.divillafajar.app.pos.pos_app_sini.io.entity.scope.ScopeEntity;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "feature")
public class FeatureEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3109169141811604281L;

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


    @Column(name = "feature_name", nullable = false, unique = true)
    private String featureName;

    // Contoh kolom tambahan
    @Column(name = "avail_time", nullable = true)
    private LocalDateTime availTime;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ScopeEntity> scopes = new HashSet<>();

    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean active = true;

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
