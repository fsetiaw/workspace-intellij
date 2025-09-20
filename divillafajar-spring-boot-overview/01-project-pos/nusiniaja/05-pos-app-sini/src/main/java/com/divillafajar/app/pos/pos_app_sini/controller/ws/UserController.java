package com.divillafajar.app.pos.pos_app_sini.controller.ws;

import com.divillafajar.app.pos.pos_app_sini.exception.GenericCustomErrorException;
import com.divillafajar.app.pos.pos_app_sini.model.user.CreateUserRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/users")
public class UserController {

    //@Autowired
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService=userService;
    }

    /*
    ** Create New User
     */
    @PostMapping("/register")
    @ResponseBody
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


    @GetMapping
    @ResponseBody
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



    @PutMapping
    @ResponseBody
    public String updateUser() {
        return "update user";
    }

    @DeleteMapping
    @ResponseBody
    public String deleteUser() {
        if(false)
            throw new GenericCustomErrorException("TEST ERROR");
        return "Delete user";
    }
}
