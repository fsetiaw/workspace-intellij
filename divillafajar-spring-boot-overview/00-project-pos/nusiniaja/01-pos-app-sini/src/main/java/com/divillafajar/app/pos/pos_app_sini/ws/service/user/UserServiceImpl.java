package com.divillafajar.app.pos.pos_app_sini.ws.service.user;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.AddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.repo.AddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.AuthRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.NamePasRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.UserRepo;
import com.divillafajar.app.pos.pos_app_sini.ws.model.shared.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final NamePasRepo namePasRepo;

    private final AuthRepo authRepo;

    private final AddressRepo addressRepo;

    public UserServiceImpl(UserRepo userRepo, NamePasRepo namePasRepo,
                           AuthRepo authRepo,AddressRepo addressRepo) {
        this.userRepo=userRepo;
        this.authRepo=authRepo;
        this.namePasRepo=namePasRepo;
        this.addressRepo=addressRepo;
    }


    @Override
    public UserDTO createUser(UserDTO userDTO) {

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);
        UserEntity storedUser = userRepo.save(userEntity);
        /*
        **  Hapus Kalu Mo Sekalian Save Address pada saat create User
        **
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUser(storedUser);
        BeanUtils.copyProperties(userDTO, addressEntity);
        AddressEntity storedAddress = addressRepo.save(addressEntity);
        System.out.println("DONE SAVE ADDRESS");

         */


        NamePassEntity nape = new NamePassEntity();
        BeanUtils.copyProperties(userDTO, nape);
        nape.setUser(storedUser);
        nape.setEnabled(true);
        NamePassEntity storedNape = namePasRepo.save(nape);

        AuthorityEntity auth = new AuthorityEntity();
        auth.setAuthority(userDTO.getAuthority());
        auth.setUsername(userDTO.getUsername());
        auth.setNamePass(storedNape);
        authRepo.save(auth);
        //nape.setUserAuth(auth);
        //System.out.println("nape = "+nape.getUser().getId());
        System.out.println("nape = "+nape.getUsername());
        System.out.println("nape = "+nape.getPassword());
        //System.out.println("nape = "+nape.getUserAuth().getAuthority());






        UserDTO returnVal = new UserDTO();
        BeanUtils.copyProperties(storedUser,returnVal);


        return returnVal;
    }
}
