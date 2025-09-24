package com.divillafajar.app.pos.pos_app_sini.repo.auth;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import org.springframework.data.repository.CrudRepository;

public interface NamePasRepo extends CrudRepository<NamePassEntity,String> {
    NamePassEntity findByUsername(String username);
    // Cek apakah ada namePass yang sudah pakai clientName tertentu
    boolean existsByClient_ClientName(String clientName);
}
