package com.divillafajar.app.pos.pos_app_sini.repo.users;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends CrudRepository<NamePassEntity,Long> {
    NamePassEntity findUsersByUsername(String username);
    // Cek apakah ada namePass yang sudah pakai clientName tertentu
    boolean existsByClient_ClientName(String clientName);

}
