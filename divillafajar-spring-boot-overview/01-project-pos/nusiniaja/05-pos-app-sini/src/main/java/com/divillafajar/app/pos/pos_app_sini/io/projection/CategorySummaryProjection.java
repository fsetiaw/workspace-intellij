package com.divillafajar.app.pos.pos_app_sini.io.projection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public interface CategorySummaryProjection {
    Long getClientAddressId();
    Long getTotalTopParent();
    Long getTotalEndChild();
    Long getTotalEndChildNoProduct();
}
