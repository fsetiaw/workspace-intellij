package com.divillafajar.app.pos.pos_app_sini.io.entity.category;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "product_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

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
}
