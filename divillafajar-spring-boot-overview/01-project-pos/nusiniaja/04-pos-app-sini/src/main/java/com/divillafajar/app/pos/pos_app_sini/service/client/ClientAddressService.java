package com.divillafajar.app.pos.pos_app_sini.service.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;

public interface ClientAddressService {
    ClientAddressDTO getStore(long aid);
    ClientAddressDTO addNewStoreOrBranch(long aid, ClientAddressDTO newStore, ClientContactDTO pic);
}
