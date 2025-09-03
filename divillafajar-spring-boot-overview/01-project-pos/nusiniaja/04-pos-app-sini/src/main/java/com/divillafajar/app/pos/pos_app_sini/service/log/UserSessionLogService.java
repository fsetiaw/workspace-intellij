package com.divillafajar.app.pos.pos_app_sini.service.log;

import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLogDTO;

public interface UserSessionLogService {
    UserSessionLogDTO prepUserSessionLog(String username);
}
