package com.divillafajar.app.pos.pos_app_sini.service.client;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.client.ClientAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientContactEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientAddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientContactRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService{
    private final ClientRepo clientRepo;
    private final ClientAddressRepo clientAddressRepo;
    private final ClientContactRepo clientContactRepo;
    private final CustomDefaultProperties customDefaultProperties;

    public ClientServiceImpl(ClientAddressRepo clientAddressRepo, ClientRepo clientRepo,
                             ClientContactRepo clientContactRepo, CustomDefaultProperties customDefaultProperties) {
        this.clientAddressRepo = clientAddressRepo;
        this.clientRepo=clientRepo;
        this.customDefaultProperties=customDefaultProperties;
        this.clientContactRepo=clientContactRepo;
    }

    @Override
    @Transactional
    public ClientDTO createSuperClient(ClientDTO clientDTO, AddressDTO addressDTO, ClientContactDTO contactDTO,  String pubId, String key) {
        //initialize return value
        ClientDTO returnVal = new ClientDTO();

        /*
        ** Cek bila user authorized untuk create super client
         */
        if(key.compareTo(customDefaultProperties.getAdminSuperKey())!=0) {
            throw new ClientAlreadyExistException("Unauthorize User Request");
        }

        //find if customer exist
        ClientEntity storedClient = clientRepo.findClientByPubId(pubId);
        if(storedClient!=null)
            throw new ClientAlreadyExistException("Super Client already exist");


        try {
            //lanjut save
            ClientEntity nuClient = new ClientEntity();
            BeanUtils.copyProperties(clientDTO,nuClient);
            nuClient.setPubId(customDefaultProperties.getMasterClientPubid());
            storedClient = clientRepo.save(nuClient);

            ClientAddressEntity nuClientDetails = new ClientAddressEntity();
            BeanUtils.copyProperties(addressDTO,nuClientDetails);

            nuClientDetails.setClient(storedClient);
            ClientAddressEntity storedClientAddress =  clientAddressRepo.save(nuClientDetails);

            ClientContactEntity pic = new ClientContactEntity();
            BeanUtils.copyProperties(contactDTO,pic);

            pic.setClientAddress(storedClientAddress);
            clientContactRepo.save(pic);

            storedClient = clientRepo.findClientByPubId(pubId);
            BeanUtils.copyProperties(storedClient,returnVal);
        }
        catch (Exception e) {
            // Transaction akan rollback otomatis
            throw new CreateUserException("Gagal Membuat Client Baru"); // lempar lagi supaya trigger rollback
        }
        return returnVal;
    }

    @Override
    public List<ClientDTO> getAllClients() {
        List<ClientDTO> returnVal = new ArrayList<>();

        List<ClientEntity> ourClients = clientRepo.findByClientTypeNot("Master");
        for(ClientEntity client : ourClients) {
            ClientDTO tmp = new ClientDTO();
            BeanUtils.copyProperties(client,tmp);
            returnVal.add(tmp);
        }
        return returnVal;
    }

    @Override
    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO, AddressDTO addressDTO, ClientContactDTO contactDTO) {
        //initialize return value
        ClientDTO returnVal = new ClientDTO();

        //find if super client exist
        ClientEntity storedClient = clientRepo.findClientByClientNameAndClientPhone(clientDTO.getClientName(), clientDTO.getClientPhone());
        if(storedClient!=null)
            throw new ClientAlreadyExistException("Client name with phone number "+clientDTO.getClientPhone()+" already exist");
        clientRepo.findClientByClientNameAndClientEmail(clientDTO.getClientName(), clientDTO.getClientEmail());
        if(storedClient!=null)
            throw new ClientAlreadyExistException("Client name with email "+clientDTO.getClientEmail()+" already exist");

        try {
            //lanjut save
            ClientEntity nuClient = new ClientEntity();
            BeanUtils.copyProperties(clientDTO,nuClient);
            storedClient = clientRepo.save(nuClient);
            ClientAddressEntity nuClientDetails = new ClientAddressEntity();
            BeanUtils.copyProperties(addressDTO,nuClientDetails);
            nuClientDetails.setClient(storedClient);
            ClientAddressEntity storedClientAddress =  clientAddressRepo.save(nuClientDetails);
            ClientContactEntity pic = new ClientContactEntity();
            BeanUtils.copyProperties(contactDTO,pic);
            pic.setClientAddress(storedClientAddress);
            clientContactRepo.save(pic);
            storedClient = clientRepo.findClientByClientNameAndClientEmail(clientDTO.getClientName(),clientDTO.getClientEmail());
            BeanUtils.copyProperties(storedClient,returnVal);
        }
        catch (Exception e) {
            // Transaction akan rollback otomatis
            throw new CreateUserException("Gagal Membuat Client Baru"); // lempar lagi supaya trigger rollback
        }
        return returnVal;
    }


}
