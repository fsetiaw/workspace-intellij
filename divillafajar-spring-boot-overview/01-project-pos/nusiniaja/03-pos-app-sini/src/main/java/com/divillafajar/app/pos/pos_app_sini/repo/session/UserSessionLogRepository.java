package com.divillafajar.app.pos.pos_app_sini.repo.session;

import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.UserSessionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionLogRepository extends JpaRepository<UserSessionLog, Long> {
    Optional<UserSessionLog> findBySessionIdAndStatus(String sessionId, String status);
}