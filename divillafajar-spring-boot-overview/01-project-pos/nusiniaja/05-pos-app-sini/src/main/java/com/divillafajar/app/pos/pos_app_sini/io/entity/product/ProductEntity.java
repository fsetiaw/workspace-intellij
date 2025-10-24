package com.divillafajar.app.pos.pos_app_sini.io.entity.product;

import com.divillafajar.app.pos.pos_app_sini.common.enums.ProductStatusEnum;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// === 🔹 Informasi dasar produk ===
	@Column(nullable = false, length = 255)
	private String name;

	@Column(length = 100, unique = true)
	private String sku; // kode unik produk

	@Column(length = 100)
	private String barcode;

	@Column(columnDefinition = "TEXT")
	private String description;

	// === 🔹 Harga dan stok ===
	@Column(precision = 15, scale = 2)
	private BigDecimal price;

	@Column(name = "cost_price", precision = 15, scale = 2)
	private BigDecimal costPrice;

	@Column(name = "discount_percent", precision = 5, scale = 2)
	private BigDecimal discountPercent;

	@Column(name = "stock_quantity")
	private Integer stockQuantity;

	@Column(name = "min_stock_alert")
	private Integer minStockAlert;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBackup; //

    private String imageType;

	// === 🔹 Atribut umum ===
	@Column(length = 50)
	private String unit; // pcs, box, kg, liter, dll

	@Column(length = 50)
	private String color;

	@Column(length = 50)
	private String size;

	@Column(length = 100)
	private String brand;

	@Column(length = 100)
	private String model;

	// === 🔹 Status ===
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ProductStatusEnum status;

	// === 🔹 Media ===
	@Column(name = "image_url", length = 255)
	private String imageUrl;

	@Column(name = "thumbnail_url", length = 255)
	private String thumbnailUrl;

	// === 🔹 Relasi utama ===

	// 1️⃣ Setiap produk milik satu kategori
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private ProductCategoryEntity category;

	// 2️⃣ Setiap produk milik satu alamat client (bisnis / cabang)
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
/*
document.addEventListener('DOMContentLoaded', function () {
	document.querySelectorAll('.toggle-edit').forEach(btn => {
			btn.addEventListener('click', function (e) {
			e.preventDefault();
                const targetId = this.getAttribute('data-target-id');
                const cardBody = this.closest('.card-body');
                const desc = cardBody.querySelector('.desc');
                const collapse = cardBody.querySelector(`#${targetId}`);
                const bsCollapse = new bootstrap.Collapse(collapse, { toggle: false });

	// toggle collapse
	if (collapse.classList.contains('show')) {
		bsCollapse.hide();
		desc.style.display = ''; // tampilkan kembali section
	} else {
		bsCollapse.show();
		desc.style.display = 'none'; // sembunyikan section
	}
            });
        });
});

 */
