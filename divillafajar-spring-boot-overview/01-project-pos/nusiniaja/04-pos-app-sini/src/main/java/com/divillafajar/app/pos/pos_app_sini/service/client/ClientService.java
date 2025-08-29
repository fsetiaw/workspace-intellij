package com.divillafajar.app.pos.pos_app_sini.service.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;

import java.util.List;

public interface ClientService {
    ClientDTO createClient(ClientDTO clientDTO, AddressDTO addressDTO, ClientContactDTO contactDTO);
    ClientDTO createSuperClient(ClientDTO clientDTO, AddressDTO addressDTO, ClientContactDTO contact, String pubId, String key);
    List<ClientDTO> getAllClients();
}

