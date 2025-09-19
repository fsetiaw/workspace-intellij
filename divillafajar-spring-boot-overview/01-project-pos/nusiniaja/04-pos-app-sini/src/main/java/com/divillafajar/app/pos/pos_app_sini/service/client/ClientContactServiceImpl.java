package com.divillafajar.app.pos.pos_app_sini.service.client;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientContactEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientContactDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientContactRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientContactServiceImpl implements ClientContactService{
    private final ClientContactRepo contactRepo;

    @Override
    public ClientContactDTO findClientContactByAddressId(Long aid) {
        ClientContactDTO retVal = new ClientContactDTO();
        System.out.println("@findClientContactByAddressId ="+aid);
        ClientContactEntity contact = contactRepo.findFirstByClientAddress_Id(aid);
        if(contact==null) {
            System.out.println("@contact is null");
        }
        else {
            BeanUtils.copyProperties(contact,retVal);
            retVal.setId(null);
        }

        System.out.println("RetVal="+retVal.getContactName());
        return retVal;
    }
}
