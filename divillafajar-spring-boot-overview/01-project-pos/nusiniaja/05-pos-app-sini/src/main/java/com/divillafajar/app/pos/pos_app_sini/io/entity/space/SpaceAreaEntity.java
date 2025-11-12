package com.divillafajar.app.pos.pos_app_sini.io.entity.space;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "space_area")
public class SpaceAreaEntity {

	@PrePersist
	protected void onCreate() {
		if (this.updatedAt == null)
			this.updatedAt = LocalDateTime.now();
		if (this.deleted == null)
			this.deleted = false;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Nama area (misal: "Main Hall", "VIP Room", "Outdoor Area", dsb.)
    @Column(nullable = false, length = 255)
    private String name;

    // 🔹 Level indentasi (misal: 0 = main space, 1 = sub-area, 2 = sub-sub-area)
    @Column(name = "indent_level")
    private Long indentLevel;

    // 🔹 Parent area (nullable jika ini main space)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private SpaceAreaEntity parent;

    // 🔹 Children area (daftar sub-area)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpaceAreaEntity> children;

    // 🔹 Relasi ke client address (1 alamat bisa punya banyak space area)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_address_id")
    private ClientAddressEntity clientAddress;

    @OneToMany(mappedBy = "spaceArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuestAreaEntity> guestAreas;

    // === 🔹 Metadata ===
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean deleted = false;

    // === 🔹 Helper Method ===
    @Transient
    public boolean isMainSpace() {
        return parent == null;
    }

    @Transient
    public String getFullPathName() {
        // Contoh: "Main Hall / VIP Area / Room 3"
        if (parent != null) {
            return parent.getFullPathName() + " / " + name;
        }
        return name;
    }
}
