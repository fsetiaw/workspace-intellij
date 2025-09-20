package com.divillafajar.app.pos.pos_app_sini.service.area;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.ClientAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.area.dto.ClientAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.area.ZoneAreaRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientAddressRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class ZoneAreaServiceImpl implements ZoneAreaService {

    private final ZoneAreaRepo areaRepo;
    private final ClientAddressRepo addressRepo;

    public ZoneAreaServiceImpl(ZoneAreaRepo areaRepo,ClientAddressRepo addressRepo) {
        this.areaRepo=areaRepo;
        this.addressRepo=addressRepo;
    }

    @Override
    public ClientAreaDTO addClientNewZoneArea(ClientAreaDTO zoneArea, Long clientAddressId) {
        ClientAreaDTO returnVal = new ClientAreaDTO();
        Optional<ClientAddressEntity> clientLocation = addressRepo.findById(clientAddressId);
        if(clientLocation.isPresent()) {
            ClientAreaEntity nuArea = new ClientAreaEntity();
            BeanUtils.copyProperties(zoneArea,nuArea);
            nuArea.setClientAddress(clientLocation.get());
            ClientAreaEntity addedArea = areaRepo.save(nuArea); // ✅ berhasil
            BeanUtils.copyProperties(addedArea,returnVal);
        }
        else {
            System.out.println("addClientNewZoneArea Adress entity is null");
        }


        return returnVal;

    }

    @Override
    public Optional<ClientAreaDTO> getClientByAreaNameOrAlias(String name, String alias) {
        Optional<ClientAreaEntity> storedArea = areaRepo.findByAreaNameOrAlias(name, alias);

        if (storedArea.isEmpty()) {
            return Optional.empty();// ✅ bukan null
        }

        ClientAreaDTO returnVal = new ClientAreaDTO();
        BeanUtils.copyProperties(storedArea.get(), returnVal);

        return Optional.of(returnVal);  // ✅ bungkus DTO ke Opti
    }

    @Override
    public Optional<List<ClientAreaDTO>> getAllAreaByAddressId(Long aid) {
        List<ClientAreaEntity> daftarArea = areaRepo.findAllByClientAddress_Id(aid);

        if (daftarArea.isEmpty()) {
            return Optional.empty();  // ✅ kalau kosong, return Optional kosong
        }

        List<ClientAreaDTO> dtoList = daftarArea.stream()
                .map(entity -> {
                    ClientAreaDTO dto = new ClientAreaDTO();
                    BeanUtils.copyProperties(entity, dto);
                    return dto;
                })
                .toList();

        return Optional.of(dtoList);  // ✅ bungkus ke Optional
    }
}
