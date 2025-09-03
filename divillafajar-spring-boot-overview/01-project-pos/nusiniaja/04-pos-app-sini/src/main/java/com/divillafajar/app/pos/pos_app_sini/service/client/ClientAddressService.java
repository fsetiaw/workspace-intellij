package com.divillafajar.app.pos.pos_app_sini.service.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;

public interface ClientAddressService {
    ClientAddressDTO getStore(long aid);
}
