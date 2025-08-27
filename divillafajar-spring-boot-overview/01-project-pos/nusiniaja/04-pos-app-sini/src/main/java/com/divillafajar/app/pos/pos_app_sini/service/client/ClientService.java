package com.divillafajar.app.pos.pos_app_sini.service.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;

public interface ClientService {
    ClientDTO createClient(ClientDTO clientDTO, AddressDTO addressDTO);
    ClientDTO createSuperClient(ClientDTO clientDTO, AddressDTO addressDTO, String pubId, String key);
}

