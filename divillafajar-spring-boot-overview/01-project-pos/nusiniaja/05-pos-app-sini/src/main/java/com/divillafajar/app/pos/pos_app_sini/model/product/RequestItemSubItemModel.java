package com.divillafajar.app.pos.pos_app_sini.model.product;

import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestItemSubItemModel {
    private Long id;
    private Long parentId;
    private Long indentLevel;
    private String name;
    private ProductCategoryEntity parent;
    private String pubAid;
    //private List<ProductCategoryEntity> children;
    //private ClientAddressEntity clientAddress;
}
