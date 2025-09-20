package com.divillafajar.app.pos.pos_app_sini.repo.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepo extends CrudRepository<ClientEntity, Long> {
    ClientEntity findClientByClientNameAndClientEmail(String name, String email);
    ClientEntity findClientByClientNameAndClientPhone(String name, String phone);
    ClientEntity findClientByPubId(String pubId);
    ClientEntity findClientByClientName(String name);
    ClientEntity findClientByClientEmail(String email);
    ClientEntity findClientByClientPhone(String phone);
    List<ClientEntity> findByClientTypeNot(String clientType);
}
