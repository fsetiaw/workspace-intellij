package com.divillafajar.app.pos.pos_app_sini.service.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;

public interface ClientAddressService {
    ClientAddressDTO getStore(long aid);
    ClientAddressDTO addNewStore(UserDTO usrDTO, ClientDTO targetClient, ClientAddressDTO nuLocation);
    ClientAddressDTO addNewStoreOrBranch(long aid, ClientAddressDTO newStore, ClientContactDTO pic);
}
