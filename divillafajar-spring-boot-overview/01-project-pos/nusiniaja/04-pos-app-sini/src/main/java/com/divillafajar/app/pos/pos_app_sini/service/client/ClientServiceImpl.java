package com.divillafajar.app.pos.pos_app_sini.service.client;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.client.ClientAlreadyExistException;
import com.divillafajar.app.pos.pos_app_sini.exception.user.CreateUserException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.address.dto.AddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientDetailsEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientDetailsRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl implements ClientService{
    private final ClientRepo clientRepo;
    private final ClientDetailsRepo clientDetailsRepo;
    private final CustomDefaultProperties customDefaultProperties;

    public ClientServiceImpl(ClientDetailsRepo clientDetailsRepo, ClientRepo clientRepo, CustomDefaultProperties customDefaultProperties) {
        this.clientDetailsRepo=clientDetailsRepo;
        this.clientRepo=clientRepo;
        this.customDefaultProperties=customDefaultProperties;
    }

    @Override
    @Transactional
    public ClientDTO createSuperClient(ClientDTO clientDTO, AddressDTO addressDTO, String pubId, String key) {
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

            ClientDetailsEntity nuClientDetails = new ClientDetailsEntity();
            BeanUtils.copyProperties(addressDTO,nuClientDetails);

            nuClientDetails.setClient(storedClient);
            clientDetailsRepo.save(nuClientDetails);

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
    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO, AddressDTO addressDTO) {
        //initialize return value
        ClientDTO returnVal = new ClientDTO();

        //find if super client exist

        ClientEntity storedClient = clientRepo.findClientByPubId(customDefaultProperties.getMasterClientPubid());
        if(storedClient!=null)
            throw new ClientAlreadyExistException("Client already exist");

        try {
            //lanjut save
            ClientEntity nuClient = new ClientEntity();
            BeanUtils.copyProperties(clientDTO,nuClient);
            storedClient = clientRepo.save(nuClient);

            ClientDetailsEntity nuClientDetails = new ClientDetailsEntity();
            BeanUtils.copyProperties(addressDTO,nuClientDetails);

            nuClientDetails.setClient(storedClient);
            clientDetailsRepo.save(nuClientDetails);

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
