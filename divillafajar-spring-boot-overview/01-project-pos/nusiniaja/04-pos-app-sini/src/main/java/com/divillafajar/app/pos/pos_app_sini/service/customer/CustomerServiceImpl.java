package com.divillafajar.app.pos.pos_app_sini.service.customer;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.GuestEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.repo.auth.AuthRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.auth.NamePasRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.user.UserRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.customer.CustomerRepo;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.dto.CustomerDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.guest.GuestRepo;
import com.divillafajar.app.pos.pos_app_sini.utils.GeneratorUtils;
import com.divillafajar.app.pos.pos_app_sini.utils.MyStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{
    private final GeneratorUtils generatorUtils;
    private final CustomDefaultProperties customDefaultProperties;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepo csr;
    private final ClientRepo clientRepo;
    private final GuestRepo guestRepo;

    private NamePasRepo namePasRepo;
    //@Autowired
    private final UserRepo userRepo;

    private final AuthRepo authRepo;

    public CustomerServiceImpl(CustomerRepo csr,UserRepo userRepo,
                               NamePasRepo namePasRepo, AuthRepo authRepo,
                               PasswordEncoder passwordEncoder,
                               CustomDefaultProperties customDefaultProperties,
                               GeneratorUtils generatorUtils,
                               ClientRepo clientRepo, GuestRepo guestRepo
    ) {
        this.csr=csr;
        this.userRepo=userRepo;
        this.namePasRepo=namePasRepo;
        this.authRepo=authRepo;
        this.passwordEncoder=passwordEncoder;
        this.customDefaultProperties=customDefaultProperties;
        this.generatorUtils=generatorUtils;
        this.clientRepo=clientRepo;
        this.guestRepo=guestRepo;
    }


    @Override
    @Transactional
    public CustomerDTO loginCustomer(CustomerDTO customerDTO) {
        System.out.println("CustomerServiceImpl.loginCustomer is CALLED");
        CustomerDTO returnVal=new CustomerDTO();
        /*
        ** Note:
        ** Untuk Customer username == no hape
        ** customerDTO.getUsername() == no hape
         */
        System.out.println("customerDTO.getUsername = "+customerDTO.getUsername());
        System.out.println("customerDTO.getClientId() = "+customerDTO.getClientId());
        System.out.println("customerDTO.getTable = "+customerDTO.getTable());

        /*
        ** get data client
         */
        ClientEntity client = clientRepo.findById(customerDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id " + customerDTO.getClientId()));


        //CustomerDTO returnVal = new CustomerDTO();


        /*
         ** Cek apa no hape sudah ada di tabel customer
         *
         */
        CustomerEntity storedCustomer = csr.findCustomerByPhoneNumber(customerDTO.getUsername());//csr.findCustomerByAliasName(customerDTO.getAliasName());
        if(storedCustomer==null) {
            /*
             ** bila tidak ditemukan, cek apa sudah ada di tabel user
             * Note:
             *   user tidak terikat ke salah satu client
             */
            UserEntity storedUser = userRepo.findByEmailAndPhone(client.getClientName()+customDefaultProperties.getCustomerDummyEmail(), customerDTO.getUsername());
            if (storedUser == null) {
                try {
                    //step 1 - Create user then
                    System.out.println("CUSTOMER belum ada di USER");
                    UserEntity nuUser = new UserEntity();
                    //nuUser.setCustomer(storedCustomer);
                    nuUser.setPhone(customerDTO.getUsername());
                    nuUser.setEmail(client.getClientName()+customDefaultProperties.getCustomerDummyEmail());
                    nuUser.setPubId(generatorUtils.generatePubUserId(30));
                    String[] givenName = MyStringUtils.splitLastWord(customerDTO.getAliasName());
                    nuUser.setFirstName(givenName[0]);
                    if (givenName[1] != null && givenName[1].isBlank())
                        nuUser.setLastName(givenName[1]);
                    System.out.println("TRY SAVE USER");
                    storedUser = userRepo.save(nuUser);
                    System.out.println("USER SAVED");

                    //step 2 - Create customer
                    CustomerEntity nuCust = new CustomerEntity();
                    nuCust.setUser(storedUser);
                    nuCust.setPhoneNumber(storedUser.getPhone());
                    nuCust.setAliasName(customerDTO.getAliasName());
                    System.out.println("TRY SAVE Customer");
                    storedCustomer = csr.save(nuCust);
                    System.out.println("Customer Saved");
                    //if (true) {
                    //    throw new Exception("Simulasi error di tengah proses");
                    //}

                /*
                    STEP 3 : CREATE GuestEntity
                 */
                    GuestEntity guest = new GuestEntity();
                    guest.setCustomer(storedCustomer);
                    //guest.setClient(client);
                    System.out.println("TRY SAVE guest");
                    GuestEntity storedGuest = guestRepo.save(guest);
                    System.out.println("Guest Saved");
                    if(false) {
                    //if(storedGuest.getClient()==null || storedGuest.getCustomer()==null){
                        throw new CreateUserException("Gagal Membuat User Baru -> tidak ada client id"); // lempar lagi supaya trigger rollback
                    }
                    else {
                        //step-4 lanjut save user pwd
                        NamePassEntity nape = new NamePassEntity();
                        nape.setUsername(storedCustomer.getPhoneNumber());
                        //nape.setPassword("{bcrypt}"+bCryptPasswordEncoder.encode("NoPwd"));
                        nape.setPassword(passwordEncoder.encode(customDefaultProperties.getCustomerPwd()));

                        nape.setGuest(storedGuest);
                        nape.setEnabled(true);
                        System.out.println("TRY SAVE NamePassEntity");
                        NamePassEntity storedNape = namePasRepo.save(nape);
                        System.out.println("NamePassEntity SAVED");

                        //step-5 lanjut save authorities
                        AuthorityEntity auth = new AuthorityEntity();
                        auth.setAuthority(customDefaultProperties.getCustomerRole());
                        auth.setUsername(storedNape.getUsername());
                        auth.setNamePass(storedNape);
                        System.out.println("TRY SAVE AuthorityEntity");
                        authRepo.save(auth);
                        System.out.println("AuthorityEntity SAVED");
                    }
                }
                catch (Exception e) {
                    // Transaction akan rollback otomatis
                    System.err.println("Error saat register customer: " + e.getMessage());
                    throw new CreateUserException("Gagal Membuat User Baru"); // lempar lagi supaya trigger rollback
                }

                //step-2 create customer entity
                //List<ClientEntity> clientInfo = new ArrayList<>();
                //clientInfo.add(client);
                //CustomerEntity logInCustomerEntity = new CustomerEntity();
                //BeanUtils.copyProperties(customerDTO, logInCustomerEntity);
                //logInCustomerEntity.setPhoneNumber(MyStringUtils.cleanPhoneNumber(customerDTO.getUsername()));
                //logInCustomerEntity.setId(null);
                //logInCustomerEntity.setUser(storedUser);
                //logInCustomerEntity.setClients(listClient);
                //storedCustomer = csr.save(logInCustomerEntity);
            } else {
                //User Exist
                //1. cek apa sudah pernah menjadi tamu target client
                ////List<ClientEntity> listClientKunjungan = storedCustomer.getClients();
                boolean clintSudahDikunjungi = false;
                ////for (ClientEntity visitedClient : listClientKunjungan) {
                ////    if (visitedClient.getId() == customerDTO.getClientId()) {
                ////        clintSudahDikunjungi = true;
                ////    }
                ////}
                ////if (!clintSudahDikunjungi) {
                    //Baru pertama visit
                    //1.add info client-customer
                ////    Optional<ClientEntity> nuClient = clientRepo.findById(customerDTO.getClientId());
                ////    nuClient.ifPresent(listClientKunjungan::add);
                    //update Data Customer
                    ////storedCustomer.setClients(listClientKunjungan);
                /////    storedCustomer = csr.save(storedCustomer);
                ////}

            }
        }
        else {
            /*
             **  Customer sudah terdaftar
             */
            //1. cek apa sudah pernah menjadi tamu target client
            ////List<ClientEntity> listClientKunjungan = storedCustomer.getClients();
            boolean clintSudahDikunjungi = false;
            ////for (ClientEntity visitedClient : listClientKunjungan) {
            ////    if (visitedClient.getId() == customerDTO.getClientId()) {
            ////        clintSudahDikunjungi = true;
            ////    }
            ////}
            if (!clintSudahDikunjungi) {
                //Baru pertama visit
                //1.add info client-customer
                Optional<ClientEntity> nuClient = clientRepo.findById(customerDTO.getClientId());
                ////nuClient.ifPresent(listClientKunjungan::add);
                //update Data Customer
                ////storedCustomer.setClients(listClientKunjungan);
                storedCustomer = csr.save(storedCustomer);
            }
        }
        /*
            //prep buat create customer
            //1. set id = null, beanUtils ngasih default = 0 bukannya null(bikin error)



            if (storedCustomer == null) { // hampir mustahil, tapi kalau repo custom bisa saja
                throw new RuntimeException("Gagal menyimpan customer");
            }
            /*
             **  Cek apa sudah ada di tabel user

            UserEntity storedUser = userRepo.findUserByCustomer(storedCustomer);
            if(storedUser==null) {
                System.out.println("CUSTOMER belum ada di USER");
                UserEntity nuUser = new UserEntity();
                nuUser.setCustomer(storedCustomer);
                nuUser.setPhone(storedCustomer.getPhoneNumber());
                nuUser.setEmail(customDefaultProperties.getCustomerDummyEmail());
                nuUser.setPubId(generatorUtils.generatePubUserId(30));
                String[]givenName = MyStringUtils.splitLastWord(storedCustomer.getAliasName());
                nuUser.setFirstName(givenName[0]);
                if(givenName[1]!=null && givenName[1].isBlank())
                    nuUser.setLastName(givenName[1]);
                System.out.println("TRY SAVE USER");
                storedUser = userRepo.save(nuUser);
                System.out.println("USER SAVED");
                NamePassEntity nape = new NamePassEntity();
                nape.setUsername(storedCustomer.getPhoneNumber());
                //nape.setPassword("{bcrypt}"+bCryptPasswordEncoder.encode("NoPwd"));
                nape.setPassword(passwordEncoder.encode(customDefaultProperties.getCustomerPwd()));
                nape.setUser(storedUser);
                nape.setEnabled(true);
                System.out.println("TRY SAVE NamePassEntity");
                NamePassEntity storedNape = namePasRepo.save(nape);
                System.out.println("NamePassEntity SAVED");

                AuthorityEntity auth = new AuthorityEntity();
                auth.setAuthority(customDefaultProperties.getCustomerRole());
                auth.setUsername(storedNape.getUsername());
                auth.setNamePass(storedNape);
                System.out.println("TRY SAVE AuthorityEntity");
                authRepo.save(auth);
                System.out.println("AuthorityEntity SAVED");

            }
        }
        else {
            /*
             **  Jika ditemukan, proceed to login

            if(!storedCustomer.getAliasName().contains(customerDTO.getAliasName()) &&
                (storedCustomer.getAliasName().length()+1+customerDTO.getAliasName().length()<255)
            ) {
                //jika pakai alias baru dan field < 255 char,maka append
                storedCustomer.setAliasName(storedCustomer.getAliasName()+","+customerDTO.getAliasName());
                csr.save(storedCustomer);
            }
            System.out.println("CUSTOMER EXIST FOUND");
        }

         */

        BeanUtils.copyProperties(storedCustomer,returnVal);
        return returnVal;
    }
}
