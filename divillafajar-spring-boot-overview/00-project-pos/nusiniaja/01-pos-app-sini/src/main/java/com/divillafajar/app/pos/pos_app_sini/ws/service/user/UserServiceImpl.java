package com.divillafajar.app.pos.pos_app_sini.ws.service.user;

import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.repo.UserRepo;
import com.divillafajar.app.pos.pos_app_sini.ws.shared.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);
        System.out.println("START SAVE");
        System.out.println(userEntity.getFirstName());
        UserEntity storedUser = userRepo.save(userEntity);
        System.out.println("DONE SAVE");
        UserDTO returnVal = new UserDTO();
        BeanUtils.copyProperties(storedUser,returnVal);
        return returnVal;
    }
}
