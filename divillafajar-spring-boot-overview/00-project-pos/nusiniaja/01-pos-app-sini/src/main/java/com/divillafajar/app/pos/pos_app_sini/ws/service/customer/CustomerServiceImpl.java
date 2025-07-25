package com.divillafajar.app.pos.pos_app_sini.ws.service.customer;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.repo.AddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.AuthRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.NamePasRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.UserRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.customer.CustomerRepo;
import com.divillafajar.app.pos.pos_app_sini.ws.model.shared.dto.CustomerDTO;
import com.divillafajar.app.pos.pos_app_sini.ws.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{

    //@Autowired
    private final CustomerRepo csr;

    private NamePasRepo namePasRepo;
    //@Autowired
    private final UserRepo userRepo;

    private final AuthRepo authRepo;

    public CustomerServiceImpl(CustomerRepo csr,UserRepo userRepo,
                               NamePasRepo namePasRepo, AuthRepo authRepo) {
        this.csr=csr;
        this.userRepo=userRepo;
        this.namePasRepo=namePasRepo;
        this.authRepo=authRepo;
    }


    @Override
    public CustomerDTO createOrGetCustomer(CustomerDTO customerDTO) {
        CustomerDTO returnVal = new CustomerDTO();
        CustomerEntity customerEntity = new CustomerEntity();
        BeanUtils.copyProperties(customerDTO, customerEntity);
        /*
        ** cek apa customer hp sudah terdaftar
         */
        CustomerEntity storedCustomer = csr.findCustomerByAliasName(customerDTO.getAliasName());

        /*
        **  Jika tidak ditemukan, langsung create customer
         */
        if(storedCustomer==null) {
            //prep buat create customer
            //1. set id = null, beanUtils ngasih default = 0 bukannya null(bikin error)
            customerEntity.setId(null);
            storedCustomer = csr.save(customerEntity);
        }
        /*
        **  Cek apa sudah ada di tabel user
         */
        UserEntity storedUser = userRepo.findUserByCustomer(storedCustomer);
        if(storedUser==null) {
            System.out.println("CUSTOMER belum ada di USER");
            UserEntity nuUser = new UserEntity();
            nuUser.setCustomer(storedCustomer);
            nuUser.setFirstName(storedCustomer.getAliasName());
            storedUser = userRepo.save(nuUser);
            NamePassEntity nape = new NamePassEntity();
            nape.setUsername(storedCustomer.getPhoneNumber());
            nape.setPassword("{noop}NoPwd");
            nape.setUser(storedUser);
            nape.setEnabled(true);
            NamePassEntity storedNape = namePasRepo.save(nape);

            AuthorityEntity auth = new AuthorityEntity();
            auth.setAuthority("ROLE_CUSTOMER");
            auth.setUsername(storedNape.getUsername());
            auth.setNamePass(storedNape);
            authRepo.save(auth);

        }
        BeanUtils.copyProperties(storedCustomer,returnVal);
        return returnVal;
    }
}
