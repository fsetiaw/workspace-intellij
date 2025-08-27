package com.divillafajar.app.pos.pos_app_sini.repo.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientDetailsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailsRepo extends CrudRepository<ClientDetailsEntity, Long> {
}
