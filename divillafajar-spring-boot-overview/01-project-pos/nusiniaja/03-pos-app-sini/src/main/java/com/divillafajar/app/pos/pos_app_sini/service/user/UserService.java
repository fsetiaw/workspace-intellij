package com.divillafajar.app.pos.pos_app_sini.service.user;

import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUser(UserDTO userDTO);
}
