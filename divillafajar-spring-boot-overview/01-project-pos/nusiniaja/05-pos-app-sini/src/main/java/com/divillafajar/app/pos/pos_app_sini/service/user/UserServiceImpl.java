package com.divillafajar.app.pos.pos_app_sini.service.user;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.GenericCustomErrorException;
import com.divillafajar.app.pos.pos_app_sini.exception.client.ClientAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.EmailAlreadyRegisterException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.AddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmployeeEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmploymentEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserLogedInModel;
import com.divillafajar.app.pos.pos_app_sini.repo.address.AddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.auth.AuthRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.auth.NamePasRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.user.UserRepo;
import com.divillafajar.app.pos.pos_app_sini.exception.user.UserAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientAddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.employee.EmployeeRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.employee.EmploymentRepo;
import com.divillafajar.app.pos.pos_app_sini.utils.GeneratorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final EmploymentRepo employmentRepo;
    private final ClientAddressRepo clientAddressRepo;
    private final ClientRepo clientRepo;
    private final AddressRepo addressRepo;
    private final NamePasRepo namePasRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepo authRepo;
    private final GeneratorUtils generatorUtils;
    private final CustomDefaultProperties customDefaultProperties;
    private final MessageSource messageSource;


    @Override
    public UserDTO getUser(UserDTO userDTO) {
        CustomerEntity customerEntity = new CustomerEntity();
        //customerEntity.set

        return null;
    }

    @Override
    public UserDTO getUser(String pid) {
        UserDTO retVal = new UserDTO();
        UserEntity userEntity = userRepo.findByPubId(pid);
        if(userEntity!=null)
            BeanUtils.copyProperties(userEntity,retVal);
        return retVal;
    }
/*
    @Override
    @Transactional
    public UserDTO createSuperUser(UserDTO userDTO, AddressDTO addressDTO, String pubId) {
        UserDTO returnVal = new UserDTO();


        //cek apa super client sudah diinput
        ClientEntity storedSuperClient = clientRepo.findClientByPubId(pubId);
        if(storedSuperClient==null)
            throw new ClientAlreadyExistException("Super Client doesn't exist");
        /*
         ** cek if user already exist

        UserEntity existingUser = userRepo.findByEmailOrPhone(userDTO.getEmail(), userDTO.getPhone());

        if (existingUser!=null) {
            if(existingUser.getEmail().equalsIgnoreCase(userDTO.getEmail())
                    && existingUser.getPhone().equalsIgnoreCase(userDTO.getPhone())
            ) {
                throw new UserAlreadyExistException("User already exist");
            }
            else if(existingUser.getEmail().equalsIgnoreCase(userDTO.getEmail())) {
                throw new UserAlreadyExistException("User with that email already exist");
            }
            else {
                throw new UserAlreadyExistException("User with that phone number already exist");
            }


            // found
            //UserEntity foundUser = user.get();
        } else {
            // not found, proceed save
            try {
                UserEntity nuuser = new UserEntity();
                BeanUtils.copyProperties(userDTO, nuuser);

                /*
                ** 1. Save User

                UserEntity createdUser = userRepo.save(nuuser);

                /*
                ** 2. Save Addresss

                AddressEntity nuUserAddress = new AddressEntity();
                BeanUtils.copyProperties(addressDTO,nuUserAddress);
                nuUserAddress.setUser(createdUser);
                addressRepo.save(nuUserAddress);



                /*
                ** 3. Save New User Info to Employee table

                EmployeeEntity nuEmployee = new EmployeeEntity();
                nuEmployee.setUser(nuuser);
                EmployeeEntity savedEmployee = employeeRepo.save(nuEmployee);

                 */

                /*
                ** 4. Create New Employment

                EmploymentEntity employmentEntity = new EmploymentEntity();
                ClientAddressEntity clientAddress = clientAddressRepo.findByClientId(storedSuperClient.getId());
                employmentEntity.setClientAddress(clientAddress);
                /*employmentEntity.setEmployee(savedEmployee);
                EmploymentEntity savedEmployment = employmentRepo.save(employmentEntity);


                NamePassEntity nape = new NamePassEntity();
                BeanUtils.copyProperties(userDTO, nape);
                nape.setPassword(passwordEncoder.encode(userDTO.getPwd()));
                nape.setEmployment(savedEmployment);
                nape.setEnabled(true);
                NamePassEntity storedNape = namePasRepo.save(nape);

                AuthorityEntity auth = new AuthorityEntity();
                /*
                 ** Cek jika admin super key match,

                if(userDTO.getSuperKey().equals(customDefaultProperties.getAdminSuperKey())) {
                    auth.setAuthority("ROLE_SUPERADMIN");
                }
                else {
                    auth.setAuthority(customDefaultProperties.getUserRole());
                }

                auth.setUsername(userDTO.getUsername());
                auth.setNamePass(storedNape);
                authRepo.save(auth);
                //nape.setUserAuth(auth);

                BeanUtils.copyProperties(createdUser,returnVal);
            }
            catch (Exception e) {
                // Transaction akan rollback otomatis
                throw new CreateUserException("Gagal Membuat Super User Baru"); // lempar lagi supaya trigger rollback
            }

        }
        return returnVal;
    }
*/


    /*
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserDTO returnVal = new UserDTO();
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);

        /*
        ** cek if user already exist

        UserEntity existingUser = userRepo.findByEmailAndPhone(userDTO.getEmail(), userDTO.getPhone());
        if (existingUser!=null) {
            throw new UserAlreadyExistException("User already exist");
            // found
            //UserEntity foundUser = user.get();
        } else {
            // not found, proceed save
            userEntity.setPubId(generatorUtils.generatePubUserId(30));
            UserEntity storedUser = userRepo.save(userEntity);


            NamePassEntity nape = new NamePassEntity();
            BeanUtils.copyProperties(userDTO, nape);
            nape.setPassword(passwordEncoder.encode(userDTO.getPwd()));
            //nape.setUser(storedUser);
            nape.setEnabled(true);
            NamePassEntity storedNape = namePasRepo.save(nape);

            AuthorityEntity auth = new AuthorityEntity();
            /*
            ** Cek jika admin super key match,

            if(userDTO.getSuperKey().equals(customDefaultProperties.getAdminSuperKey())) {
                auth.setAuthority("ROLE_SUPERADMIN");
            }
            else {
                auth.setAuthority(customDefaultProperties.getUserRole());
            }

            auth.setUsername(userDTO.getUsername());
            auth.setNamePass(storedNape);
            authRepo.save(auth);
            //nape.setUserAuth(auth);

            BeanUtils.copyProperties(storedUser,returnVal);
        }
        return returnVal;
    }
    */

    @Override
    public UserLogedInModel prepUserLogedInfo(String username) {
        UserLogedInModel retVal = new UserLogedInModel();
        NamePassEntity usrPass = namePasRepo.findByUsername(username);
        Optional<AuthorityEntity> auth = authRepo.findByUsername(username);
        retVal.setUsername(usrPass.getUsername());
        if(auth.isEmpty())
            throw new GenericCustomErrorException("Auth is missing");
        Optional<UserEntity> user = userRepo.findById(usrPass.getUser().getId());

        if(user.isEmpty())
            throw new GenericCustomErrorException("User is missing");
        BeanUtils.copyProperties(user,retVal);
        return retVal;
    }

    @Transactional
    @Override
    public UserDTO createUser(String role, UserDTO userDTO, ClientDTO clientDTO) {
        UserDTO returnVal = new UserDTO();

        System.out.println("Creating superadmin--"+userDTO.getUsername().length());
        /*
         ** cek if username to short
         */
        if(userDTO.getUsername()!=null && userDTO.getUsername().trim().length()<5)
            throw new CreateUserException("Username min terdiri dari 5 char");
        /*
        Jika mo buat superdamin
         */
        if(role.compareToIgnoreCase("ROLE_SUPERADMIN")==0) {
            if(!userDTO.getSuperKey().equals(customDefaultProperties.getAdminSuperKey()))
                throw new CreateUserException("Unauthorized User Privilage");

            //cek apa superadmin sudah ada
            Optional<AuthorityEntity> existedSuper = authRepo.findByAuthority(role);
            if(existedSuper.isPresent())
                throw new UserAlreadyExistException("Super User already exist");
        }


        /*
         ** cek if username sudah digunakan
         */
        Optional<AuthorityEntity> existedSuper = authRepo.findByUsername(userDTO.getUsername());
        if(existedSuper.isPresent())
            throw new UserAlreadyExistException("Username already used");

        /*
         ** cek if user already exist
         */
        UserEntity existingUser = userRepo.findByEmail(userDTO.getEmail());
        if (existingUser!=null)
            throw new EmailAlreadyRegisterException("Email Already Registered");

        /*
        cek business name sudah digunakan sama user atau belum
         */
        if(namePasRepo.existsByClient_ClientName(clientDTO.getBusinessName()))
            throw new EmailAlreadyRegisterException(messageSource.getMessage("error.biznameAlreadyUsed", null, LocaleContextHolder.getLocale()));

        try {

            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(userDTO, userEntity);
            UserEntity storedUser = userRepo.save(userEntity);

            NamePassEntity nape = new NamePassEntity();
            BeanUtils.copyProperties(userDTO, nape);
            nape.setPassword(passwordEncoder.encode(userDTO.getPwd()));
            nape.setEnabled(true);
            nape.setUser(storedUser);
            ClientEntity newuClient = new ClientEntity();
            BeanUtils.copyProperties(clientDTO,newuClient);
            ClientEntity storedClient  = clientRepo.save(newuClient);
            nape.setClient(storedClient);
            BeanUtils.copyProperties(storedUser,returnVal);


            NamePassEntity storedNape = namePasRepo.save(nape);


            AuthorityEntity auth = new AuthorityEntity();
            auth.setAuthority(role.toUpperCase().trim());
            auth.setUsername(userDTO.getUsername());
            auth.setNamePass(storedNape);
            authRepo.save(auth);


        }
        catch (Exception e) {
            e.printStackTrace();
            throw new CreateUserException("Gagal Membuat User Baru");
        }
        return returnVal;
    }
}
