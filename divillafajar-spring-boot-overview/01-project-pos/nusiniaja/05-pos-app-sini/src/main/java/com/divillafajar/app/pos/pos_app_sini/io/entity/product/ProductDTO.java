package com.divillafajar.app.pos.pos_app_sini.io.entity.product;

import com.divillafajar.app.pos.pos_app_sini.common.enums.ProductStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDTO {
	private Long id;

	// === 🔹 Informasi dasar produk ===
	private String name;
	private String sku;
	private String barcode;
	private String description;

	// === 🔹 Harga dan stok ===
	private BigDecimal price;
	private BigDecimal costPrice;
	private BigDecimal discountPercent;
	private Integer stockQuantity;
	private Integer minStockAlert;

	// === 🔹 Atribut umum ===
	private String unit;
	private String color;
	private String size;
	private String brand;
	private String model;

	// === 🔹 Status ===
	private ProductStatusEnum status;

	// === 🔹 Media ===
	private String imageUrl;
	private String thumbnailUrl;

	// === 🔹 Relasi utama (hanya ID & nama ringkas, bukan entity penuh) ===
	private Long categoryId;
	private String categoryName;

	private Long clientAddressId;
	private String clientAddressName;
    private byte[] imageBackup;
    private String imageType;

	// === 🔹 Metadata ===
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private Boolean deleted;
}
