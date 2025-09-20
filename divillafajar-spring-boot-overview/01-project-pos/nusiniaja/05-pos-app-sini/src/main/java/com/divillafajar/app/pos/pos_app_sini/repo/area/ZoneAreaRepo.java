package com.divillafajar.app.pos.pos_app_sini.repo.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.ClientAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.dto.ClientAreaDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneAreaRepo extends CrudRepository<ClientAreaEntity, Long> {
    Optional<ClientAreaEntity> findByAreaNameOrAlias(String name, String alias);
    // 1. Berdasarkan entity ClientAddress langsung
    List<ClientAreaEntity> findAllByClientAddress(ClientAddressEntity clientAddress);

    // 2. Berdasarkan ID (lebih sering dipakai)
    List<ClientAreaEntity> findAllByClientAddress_Id(Long clientAddressId);
}
