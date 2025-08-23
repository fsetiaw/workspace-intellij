package com.divillafajar.app.pos.pos_app_sini.ws.controller.superadmin;

import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;
import com.divillafajar.app.pos.pos_app_sini.model.client.ClientDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.model.client.CreateClientRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.superman.CreateSuperUserRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.user.CreateUserRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/superman")
public class SuperAdminController {

    private final UserService userService;


    public SuperAdminController(UserService userService) {
        this.userService=userService;
    }

    @PostMapping("/register")
    @ResponseBody
    public UserDetailsResponseModel createSuperUser(@RequestBody CreateSuperUserRequestModel userDetailsRequestModel) {

        System.out.println("firstname = "+userDetailsRequestModel.getFirstName());
        UserDetailsResponseModel returnVal = new UserDetailsResponseModel();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetailsRequestModel, userDTO);

        System.out.println("userDTO firstname = "+userDTO.getFirstName());

        UserDTO createUser = userService.createUser(userDTO);
        BeanUtils.copyProperties(createUser,returnVal);

        return returnVal;
    }

    @PostMapping("/register/client")
    @ResponseBody
    public UserDetailsResponseModel createSuperUser(@RequestBody CreateClientRequestModel userDetailsRequestModel) {

        System.out.println("firstname = "+userDetailsRequestModel.getClientName());
        ClientDetailsResponseModel returnVal = new ClientDetailsResponseModel();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetailsRequestModel, userDTO);

        System.out.println("userDTO firstname = "+userDTO.getFirstName());

        UserDTO createUser = userService.createUser(userDTO);
        BeanUtils.copyProperties(createUser,returnVal);

        return returnVal;
    }
}
