package com.divillafajar.app.pos.pos_app_sini.ws.service.user;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.AddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.repo.AddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.AuthRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.NamePasRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.UserRepo;
import com.divillafajar.app.pos.pos_app_sini.ws.shared.dto.UserDTO;
import org.hibernate.boot.model.source.spi.VersionAttributeSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    NamePasRepo namePasRepo;

    @Autowired
    AuthRepo authRepo;

    @Autowired
    AddressRepo addressRepo;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);
        UserEntity storedUser = userRepo.save(userEntity);
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUser(storedUser);
        BeanUtils.copyProperties(userDTO, addressEntity);
        AddressEntity storedAddress = addressRepo.save(addressEntity);
        System.out.println("DONE SAVE ADDRESS");


        NamePassEntity nape = new NamePassEntity();
        BeanUtils.copyProperties(userDTO, nape);
        nape.setUser(storedUser);
        nape.setEnabled(true);

        AuthorityEntity auth = new AuthorityEntity();
        auth.setAuthority(userDTO.getAuthority());
        auth.setUserNamePass(nape);
        nape.setUserAuth(auth);
        System.out.println("nape = "+nape.getUser().getId());
        System.out.println("nape = "+nape.getUsername());
        System.out.println("nape = "+nape.getPassword());
        System.out.println("nape = "+nape.getUserAuth().getAuthority());
        NamePassEntity StoredNape = namePasRepo.save(nape);





        UserDTO returnVal = new UserDTO();
        BeanUtils.copyProperties(storedUser,returnVal);


        return returnVal;
    }
}
