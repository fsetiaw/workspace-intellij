package com.divillafajar.app.pos.pos_app_sini.service.user;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.client.ClientAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.AddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmployeeEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmploymentEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.repo.address.AddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.auth.AuthRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.auth.NamePasRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.user.UserRepo;
import com.divillafajar.app.pos.pos_app_sini.exception.user.UserAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientDetailsRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.employee.EmployeeRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.employee.EmploymentRepo;
import com.divillafajar.app.pos.pos_app_sini.utils.GeneratorUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final EmploymentRepo employmentRepo;
    private final ClientDetailsRepo clientDetailsRepo;
    private final ClientRepo clientRepo;
    private final AddressRepo addressRepo;
    private final NamePasRepo namePasRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepo authRepo;
    private final GeneratorUtils generatorUtils;
    private final CustomDefaultProperties customDefaultProperties;

    public UserServiceImpl(UserRepo userRepo, NamePasRepo namePasRepo,
                           AuthRepo authRepo,ClientRepo clientRepo,ClientDetailsRepo clientDetailsRepo,
                           GeneratorUtils generatorUtils,AddressRepo addressRepo,EmploymentRepo employmentRepo,
                           CustomDefaultProperties customDefaultProperties,EmployeeRepo employeeRepo,
                           PasswordEncoder passwordEncoder
    ) {
        this.userRepo=userRepo;
        this.authRepo=authRepo;
        this.namePasRepo=namePasRepo;
        this.addressRepo=addressRepo;
        this.passwordEncoder=passwordEncoder;
        this.generatorUtils=generatorUtils;
        this.clientRepo=clientRepo;
        this.clientDetailsRepo=clientDetailsRepo;
        this.customDefaultProperties=customDefaultProperties;
        this.employeeRepo=employeeRepo;
        this.employmentRepo=employmentRepo;
    }


    @Override
    public UserDTO getUser(UserDTO userDTO) {
        CustomerEntity customerEntity = new CustomerEntity();
        //customerEntity.set

        return null;
    }

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
         */
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
                 */
                UserEntity createdUser = userRepo.save(nuuser);

                /*
                ** 2. Save Addresss
                 */
                AddressEntity nuUserAddress = new AddressEntity();
                BeanUtils.copyProperties(addressDTO,nuUserAddress);
                nuUserAddress.setUser(createdUser);
                addressRepo.save(nuUserAddress);

                /*
                ** 3. Save New User Info to Employee table
                 */
                EmployeeEntity nuEmployee = new EmployeeEntity();
                nuEmployee.setUser(nuuser);
                EmployeeEntity savedEmployee = employeeRepo.save(nuEmployee);

                /*
                ** 4. Create New Employment
                 */
                EmploymentEntity employmentEntity = new EmploymentEntity();
                //ClientEntity masterClient = clientRepo.findClientByClientNameAndClientEmail(
                //        customDefaultProperties.getMasterClientName(),customDefaultProperties.getMasterClientEmail());
                //employmentEntity.setClient(masterClient);
                employmentEntity.setClient(storedSuperClient);
                employmentEntity.setEmployee(savedEmployee);
                EmploymentEntity savedEmployment = employmentRepo.save(employmentEntity);


                NamePassEntity nape = new NamePassEntity();
                BeanUtils.copyProperties(userDTO, nape);
                nape.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                nape.setEmployment(savedEmployment);
                nape.setEnabled(true);
                NamePassEntity storedNape = namePasRepo.save(nape);

                AuthorityEntity auth = new AuthorityEntity();
                /*
                 ** Cek jika admin super key match,
                 */
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


            NamePassEntity nape = new NamePassEntity();
            BeanUtils.copyProperties(userDTO, nape);
            nape.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            //nape.setUser(storedUser);
            nape.setEnabled(true);
            NamePassEntity storedNape = namePasRepo.save(nape);

            AuthorityEntity auth = new AuthorityEntity();
            /*
            ** Cek jika admin super key match,
             */
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
}
