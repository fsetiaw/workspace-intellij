package com.divillafajar.app.pos.pos_app_sini.io.entity.category;

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
@Table(name = "product_category")
    //,uniqueConstraints = {
    //        @UniqueConstraint(columnNames = {"name"})
    //})
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "indent_level")
    private Long indentLevel;

    // Parent category (nullable untuk root)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ProductCategoryEntity parent;

    // Children category
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategoryEntity> children;

    // 🔹 Relasi ke ClientAddress (ManyToOne, karena 1 kategori milik 1 alamat)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_address_id")
    private ClientAddressEntity clientAddress;

	// === 🔹 Metadata ===
	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "updated_by", length = 100)
	private String updatedBy;

	@Column(name = "deleted")
	private Boolean deleted = false;
}
