package com.divillafajar.app.pos.pos_app_sini.io.entity.category;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDTO {
    private Long id;
    private String name;
    private ProductCategoryEntity parent;
    private List<ProductCategoryEntity> children;
    private ClientAddressEntity clientAddress;
}
