package com.divillafajar.app.pos.pos_app_sini.repo.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientAddressRepo extends CrudRepository<ClientAddressEntity, Long> {
    ClientAddressEntity findByClientId(Long clientId);
    ClientAddressEntity findById(long id);
    List<ClientAddressEntity> findByClientIdAndAddressNameIgnoreCaseAndAddressNicknameIgnoreCase(
            Long clientId, String addressName, String addressNickname);
}
