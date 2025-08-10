package com.divillafajar.app.pos.pos_app_sini.repo;

import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserSessionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSessionHistoryRepo extends JpaRepository<UserSessionHistory, Long> {
    List<UserSessionHistory> findByActiveTrue();
}
