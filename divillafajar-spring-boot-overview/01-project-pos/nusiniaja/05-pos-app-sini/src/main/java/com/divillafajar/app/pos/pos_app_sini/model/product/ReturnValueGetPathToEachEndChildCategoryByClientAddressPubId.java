package com.divillafajar.app.pos.pos_app_sini.model.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId {
    private String fullPath;
    private Long categoryId;
    private Long totalProducts;
}
