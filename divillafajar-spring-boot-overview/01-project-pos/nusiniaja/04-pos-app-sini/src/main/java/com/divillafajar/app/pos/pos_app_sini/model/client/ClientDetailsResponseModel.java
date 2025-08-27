package com.divillafajar.app.pos.pos_app_sini.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDetailsResponseModel {
    private String pubId;
    private String clientName;
    private String status;
}
