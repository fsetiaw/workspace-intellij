package com.divillafajar.app.pos.pos_app_sini.service.user;

import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO createSuperUser(UserDTO userDTO, AddressDTO addressDTO,String pubId);
    UserDTO getUser(UserDTO userDTO);
}
