package com.divillafajar.app.pos.pos_app_sini.controller.ws.register;

import com.divillafajar.app.pos.pos_app_sini.exception.client.ClientAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.exception.model.GenericErrorResponse;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.UserAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;
import com.divillafajar.app.pos.pos_app_sini.model.client.ClientDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.model.client.CreateClientRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.superuser.CreateSuperUserRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserDetailsResponseModel;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserRegistrationRequestModel;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/register")
@RequiredArgsConstructor
public class RegistrationControllerV1 {

    private final UserService userService;
    private final ClientService clientService;




    @PostMapping("/client/{pubId}/super/{key}")
    public ResponseEntity<?> createSuperClient(@RequestBody CreateClientRequestModel createClientRequestModel
            , @PathVariable("pubId") String pubId, @PathVariable("key") String key) {
        try {
            ClientDetailsResponseModel returnVal = new ClientDetailsResponseModel();

            ClientDTO client = new ClientDTO();
            BeanUtils.copyProperties(createClientRequestModel,client);

            AddressDTO address = new AddressDTO();
            BeanUtils.copyProperties(createClientRequestModel,address);

            ClientContactDTO pic = new ClientContactDTO();
            BeanUtils.copyProperties(createClientRequestModel,pic);


            ClientDTO createdClient = clientService.createSuperClient(client,address, pic, pubId, key);

            BeanUtils.copyProperties(createdClient,returnVal);

            return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);
        } catch (ClientAlreadyExistException ex) {
            // gagal server / unexpected error
            GenericErrorResponse error = new GenericErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (CreateUserException ex) {
            // gagal server / unexpected error
            GenericErrorResponse error = new GenericErrorResponse("Invalid Data Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception ex) {
            // gagal server / unexpected error
            GenericErrorResponse error = new GenericErrorResponse("Unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
/*
    @PostMapping("/client")
    public ResponseEntity<?> createClient(@RequestBody CreateClientRequestModel createClientRequestModel) {
        try {
            ClientDetailsResponseModel returnVal = new ClientDetailsResponseModel();

            ClientDTO client = new ClientDTO();
            BeanUtils.copyProperties(createClientRequestModel,client);

            AddressDTO address = new AddressDTO();
            BeanUtils.copyProperties(createClientRequestModel,address);

            ClientDTO createdClient = clientService.createClient(client,address);

            BeanUtils.copyProperties(createdClient,returnVal);

            return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);
        } catch (ClientAlreadyExistException ex) {
            // gagal server / unexpected error
            GenericErrorResponse error = new GenericErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (CreateUserException ex) {
            // gagal server / unexpected error
            GenericErrorResponse error = new GenericErrorResponse("Invalid Data Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception ex) {
            // gagal server / unexpected error
            GenericErrorResponse error = new GenericErrorResponse("Unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }


    }

 */

    @PostMapping("/thor")
    public ResponseEntity<?> createSuperUser(@RequestBody UserRegistrationRequestModel userRegistrationRequestModel) {
        UserDetailsResponseModel returnVal = new UserDetailsResponseModel();
        System.out.println("createSuperUser is called ="+userRegistrationRequestModel.getUsername());
        try {


            UserDTO newUser = new UserDTO();
            BeanUtils.copyProperties(userRegistrationRequestModel,newUser);

            //AddressDTO addressDTO = new AddressDTO();
            //BeanUtils.copyProperties(createSuperUserRequestModel,addressDTO);

            //UserDTO createUser = userService.createSuperUser(userDTO,addressDTO,pubId);

            //UserDTO createUser = userService.createUser("ROLE_SUPERADMIN", newUser, clientDto);
            //BeanUtils.copyProperties(createUser,returnVal);
            return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);
        } catch (UserAlreadyExistException ex) {
            // gagal server / unexpected error
            GenericErrorResponse error = new GenericErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (CreateUserException ex) {
            // gagal server / unexpected error
            GenericErrorResponse error = new GenericErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception ex) {
            // gagal server / unexpected error
            GenericErrorResponse error = new GenericErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
