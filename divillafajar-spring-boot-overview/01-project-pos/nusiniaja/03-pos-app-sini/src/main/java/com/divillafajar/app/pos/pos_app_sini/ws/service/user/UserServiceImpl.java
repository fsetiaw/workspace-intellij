package com.divillafajar.app.pos.pos_app_sini.ws.service.user;

import com.divillafajar.app.pos.pos_app_sini.config.security.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.repo.AddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.AuthRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.NamePasRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.UserRepo;
import com.divillafajar.app.pos.pos_app_sini.ws.exception.user.UserAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.ws.model.shared.dto.UserDTO;
import com.divillafajar.app.pos.pos_app_sini.ws.utils.GeneratorUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final NamePasRepo namePasRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepo authRepo;
    private final GeneratorUtils generatorUtils;
    private final AddressRepo addressRepo;
    private final CustomDefaultProperties customDefaultProperties;

    public UserServiceImpl(UserRepo userRepo, NamePasRepo namePasRepo,
                           AuthRepo authRepo,AddressRepo addressRepo,
                           GeneratorUtils generatorUtils,
                           CustomDefaultProperties customDefaultProperties,
                           PasswordEncoder passwordEncoder
    ) {
        this.userRepo=userRepo;
        this.authRepo=authRepo;
        this.namePasRepo=namePasRepo;
        this.addressRepo=addressRepo;
        this.passwordEncoder=passwordEncoder;
        this.generatorUtils=generatorUtils;
        this.customDefaultProperties=customDefaultProperties;
    }


    @Override
    public UserDTO getUser(UserDTO userDTO) {
        CustomerEntity customerEntity = new CustomerEntity();
        //customerEntity.set

        return null;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserDTO returnVal = new UserDTO();
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);

        /*
        ** cek if user already exist
         */
        UserEntity existingUser = userRepo.findByEmailAndPhone(userDTO.getEmail(), userDTO.getPhone());
        if (existingUser!=null) {
            throw new UserAlreadyExistException("User already exist");
            // found
            //UserEntity foundUser = user.get();
        } else {
            // not found, proceed save
            userEntity.setPubId(generatorUtils.generatePubUserId(30));
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
            nape.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            nape.setUser(storedUser);
            nape.setEnabled(true);
            NamePassEntity storedNape = namePasRepo.save(nape);

            AuthorityEntity auth = new AuthorityEntity();
            auth.setAuthority(customDefaultProperties.getUserRole());
            auth.setUsername(userDTO.getUsername());
            auth.setNamePass(storedNape);
            authRepo.save(auth);
            //nape.setUserAuth(auth);
            //System.out.println("nape = "+nape.getUser().getId());
            System.out.println("nape = "+nape.getUsername());
            System.out.println("nape = "+nape.getPassword());

            BeanUtils.copyProperties(storedUser,returnVal);
        }

        //System.out.println("nape = "+nape.getUserAuth().getAuthority());









        return returnVal;
    }
}
