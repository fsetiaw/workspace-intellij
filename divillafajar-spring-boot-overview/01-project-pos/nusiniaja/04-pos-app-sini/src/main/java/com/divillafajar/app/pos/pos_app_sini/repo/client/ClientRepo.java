package com.divillafajar.app.pos.pos_app_sini.repo.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends CrudRepository<ClientEntity, Long> {
    ClientEntity findClientByClientNameAndClientEmail(String name, String email);
    ClientEntity findClientByPubId(String pubId);
}
