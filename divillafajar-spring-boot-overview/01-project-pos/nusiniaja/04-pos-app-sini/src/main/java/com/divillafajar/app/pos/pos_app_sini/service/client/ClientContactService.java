package com.divillafajar.app.pos.pos_app_sini.service.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientContactEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;

public interface ClientContactService {
    ClientContactDTO findClientContactByAddressId(Long aid);
}
