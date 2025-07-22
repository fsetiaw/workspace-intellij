package com.divillafajar.app.pos.pos_app_sini.ws.controller;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import com.divillafajar.app.pos.pos_app_sini.ws.model.user.CreateUserRequestModel;
import com.divillafajar.app.pos.pos_app_sini.ws.model.user.UserDetailsRequestModel;
import com.divillafajar.app.pos.pos_app_sini.ws.model.user.UserDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.ws.service.user.UserService;
import com.divillafajar.app.pos.pos_app_sini.ws.shared.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/hello")
    public String hello(Model theModel) {
        theModel.addAttribute("theDate", java.time.LocalDateTime.now());
        return  "hellopageuser";
    }



    @GetMapping
    public String getUser() {
        return "get user";
    }
/*
    @PostMapping
    public UserDetailsResponseModel createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) {
        System.out.println("firstname = "+userDetailsRequestModel.getFirstName());
        UserDetailsResponseModel returnVal = new UserDetailsResponseModel();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetailsRequestModel, userDTO);
        System.out.println("userDTO firstname = "+userDTO.getFirstName());

        UserDTO createUser = userService.createUser(userDTO);
        BeanUtils.copyProperties(createUser,returnVal);
        return returnVal;
    }

 */

    @PostMapping
    public UserDetailsResponseModel createUser(@RequestBody CreateUserRequestModel userDetailsRequestModel) {

        System.out.println("firstname = "+userDetailsRequestModel.getFirstName());
        UserDetailsResponseModel returnVal = new UserDetailsResponseModel();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetailsRequestModel, userDTO);

        System.out.println("userDTO firstname = "+userDTO.getFirstName());

        UserDTO createUser = userService.createUser(userDTO);
        BeanUtils.copyProperties(createUser,returnVal);

        return returnVal;
    }

    @PutMapping
    public String updateUser() {
        return "update user";
    }

    @DeleteMapping
    public String deleteUser() {
        return "Delete user";
    }
}
