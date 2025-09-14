package com.divillafajar.app.pos.pos_app_sini.service.client;

import ch.qos.logback.core.net.server.Client;
import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientContactEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientAddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientContactRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientAddressServiceImpl implements ClientAddressService{

    private final ClientAddressRepo clientAddressRepo;
    private final ClientRepo clientRepo;
    private final ClientContactRepo clientContactRepo;

    public ClientAddressServiceImpl(ClientContactRepo clientContactRepo,
    ClientAddressRepo clientAddressRepo, ClientRepo clientRepo) {
        this.clientAddressRepo=clientAddressRepo;
        this.clientRepo=clientRepo;
        this.clientContactRepo=clientContactRepo;
    }

    @Override
    public ClientAddressDTO getStore(long aid) {
        ClientAddressDTO returnVal = new ClientAddressDTO();
        ClientAddressEntity store = clientAddressRepo.findById(aid);
        System.out.println("getListStore store="+store.getAddressName());
        BeanUtils.copyProperties(store,returnVal);
        return returnVal;
    }

    @Override
    @Transactional
    public ClientAddressDTO addNewStoreOrBranch(long aid, ClientAddressDTO newStore, ClientContactDTO pic) {
        ClientAddressDTO retVal = new ClientAddressDTO();
        System.out.println("addNewStoreOrBranch newStore name="+newStore.getAddressName());
        System.out.println("newStore nickname="+newStore.getAddressNickname());
        List<ClientAddressEntity> existingStore = clientAddressRepo.findByClientIdAndAddressNameIgnoreCaseAndAddressNicknameIgnoreCase(aid, newStore.getAddressName().trim(), newStore.getAddressNickname().trim());
        if(existingStore!=null && existingStore.size()>0) {
            System.out.println("Store Existed = "+existingStore.get(0).getAddressName());
            throw new DuplicationErrorException("location");
        }
        //get client id
        ClientAddressEntity stored = clientAddressRepo.findById(aid);


        Optional<ClientEntity> client = clientRepo.findById(stored.getClient().getId());
        if(client.isEmpty())
            throw new RuntimeException("Null Client");
        ClientContactEntity nuPic = new ClientContactEntity();
        BeanUtils.copyProperties(pic, nuPic);

        ClientAddressEntity nuAddress = new ClientAddressEntity();
        BeanUtils.copyProperties(newStore,nuAddress);
        nuAddress.setClient(client.get());
        nuPic.setClientAddress(nuAddress);
        ClientContactEntity newContact = clientContactRepo.save(nuPic);
        Optional<ClientAddressEntity> newStoredAddress= clientAddressRepo.findById(newContact.getClientAddress().getId());
        if(newStoredAddress.isEmpty())
            throw new RuntimeException("Null newStoredAddress");
        BeanUtils.copyProperties(newStoredAddress,retVal);

        return retVal;
    }
}
