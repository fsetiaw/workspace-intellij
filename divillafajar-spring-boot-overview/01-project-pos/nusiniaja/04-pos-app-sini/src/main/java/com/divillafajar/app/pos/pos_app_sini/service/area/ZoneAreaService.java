package com.divillafajar.app.pos.pos_app_sini.service.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.dto.ClientAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;

import java.util.List;
import java.util.Optional;

public interface ZoneAreaService {
    ClientAreaDTO addClientNewZoneArea(ClientAreaDTO zoneArea, Long clientAddressId);
    Optional<ClientAreaDTO> getClientByAreaNameOrAlias(String name, String alias);
    Optional<List<ClientAreaDTO>> getAllAreaByAddressId(Long aid);
}
