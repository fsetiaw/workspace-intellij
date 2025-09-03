package com.divillafajar.app.pos.pos_app_sini.service.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientAddressRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ClientAddressServiceImpl implements ClientAddressService{

    private final ClientAddressRepo clientAddressRepo;

    public ClientAddressServiceImpl(ClientAddressRepo clientAddressRepo) {
        this.clientAddressRepo=clientAddressRepo;
    }

    @Override
    public ClientAddressDTO getStore(long aid) {
        ClientAddressDTO returnVal = new ClientAddressDTO();
        ClientAddressEntity store = clientAddressRepo.findById(aid);
        System.out.println("getListStore store="+store.getAddressName());
        BeanUtils.copyProperties(store,returnVal);
        return returnVal;
    }
}
