package com.divillafajar.app.pos.pos_app_sini.service.user;

import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserLogedInModel;

public interface UserService {
    //UserDTO createUser(UserDTO userDTO);
    UserLogedInModel prepUserLogedInfo(String username);
    UserDTO createUser(String role,UserDTO userDTO, ClientDTO clientDTO);
    //UserDTO createSuperUser(UserDTO userDTO, AddressDTO addressDTO,String pubId);
    UserDTO getUser(UserDTO userDTO);
    UserDTO getUser(String pid);
}
