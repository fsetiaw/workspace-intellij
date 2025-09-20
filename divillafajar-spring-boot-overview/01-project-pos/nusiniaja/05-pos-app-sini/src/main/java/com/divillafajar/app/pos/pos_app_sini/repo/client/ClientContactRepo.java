package com.divillafajar.app.pos.pos_app_sini.repo.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientContactEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientContactRepo extends CrudRepository<ClientContactEntity, Long> {
    // cari kontak berdasarkan clientAddress.id
    List<ClientContactEntity> findByClientAddress_Id(Long clientAddressId);

    // kalau hanya butuh 1 (misal ambil first)
    ClientContactEntity findFirstByClientAddress_Id(Long clientAddressId);
}
