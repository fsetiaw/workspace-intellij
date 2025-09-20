package com.divillafajar.app.pos.pos_app_sini.controller.ws.superuser;

import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;
import com.divillafajar.app.pos.pos_app_sini.model.client.ClientDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.model.client.CreateClientRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.superuser.CreateSuperUserRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/superuser")
public class SuperUserController {

    private final UserService userService;
    private final ClientService clientService;

    public SuperUserController(UserService userService, ClientService clientService) {
        this.userService=userService;
        this.clientService=clientService;
    }







}
