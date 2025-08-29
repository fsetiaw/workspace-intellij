package com.divillafajar.app.pos.pos_app_sini.repo.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientContactEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientContactRepo extends CrudRepository<ClientContactEntity, Long> {
}
