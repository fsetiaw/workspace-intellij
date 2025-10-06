package com.divillafajar.app.pos.pos_app_sini.service.product.category;

import com.divillafajar.app.pos.pos_app_sini.config.properties.CustomDefaultProperties;
import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.exception.GenericCustomErrorException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientAddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.product.category.ProductCategoryRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService{

    private final ProductCategoryRepo catRepo;
    private final ClientAddressRepo addressRepo;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Override
    public ProductCategoryDTO addNewProductCategory(String categoryName, String pAid) throws Exception {
        ProductCategoryDTO retVal = new ProductCategoryDTO();
        System.out.println("addNewProductCategory="+categoryName+"~"+pAid);
        ClientAddressEntity targetLocation = addressRepo.findByPubId(pAid);
        if(targetLocation==null) {
            throw new NullPointerException("Location not found");
        }
        Optional<ProductCategoryEntity> existed = catRepo.findByNameIgnoreCaseAndClientAddress_Id(categoryName, targetLocation.getId());
        if(existed.isPresent()) {
            throw new DuplicationErrorException(messageSource.getMessage("label.manager.setting.product.category", null, Locale.getDefault())+": "+categoryName+", "+messageSource.getMessage("msg.isUsed", null, Locale.getDefault()));
        }
        ProductCategoryEntity newCat = new ProductCategoryEntity();
        newCat.setName(categoryName);
        newCat.setClientAddress(targetLocation);
        try {
            ProductCategoryEntity saved = catRepo.save(newCat);
            BeanUtils.copyProperties(saved,retVal);
        }
        catch (Exception e) {
            throw new GenericCustomErrorException("Unexpected Error");
        }
        return retVal;
    }

    @Override
    public ProductCategoryDTO addSubProductCategory(Long parentId, String categoryName, String pAid) {
        ProductCategoryDTO retVal = new ProductCategoryDTO();

        ClientAddressEntity targetLocation = addressRepo.findByPubId(pAid);
        if(targetLocation==null)
            throw new NullPointerException("Location not found");

        Optional<ProductCategoryEntity> existed = catRepo.findByNameIgnoreCaseAndClientAddress_Id(categoryName, targetLocation.getId());
        if(existed.isPresent())
            throw new DuplicationErrorException(messageSource.getMessage("label.manager.setting.product.category", null, Locale.getDefault())+": "+categoryName+", "+messageSource.getMessage("msg.isUsed", null, Locale.getDefault()));

        Optional<ProductCategoryEntity> parentEntity = catRepo.findById(parentId);
        if(parentEntity.isEmpty())
            throw new NullPointerException("Parent Category Not Found");

        ProductCategoryEntity newCat = new ProductCategoryEntity();
        newCat.setName(categoryName);
        newCat.setClientAddress(targetLocation);
        newCat.setParent(parentEntity.get());
        try {
            ProductCategoryEntity saved = catRepo.save(newCat);
            BeanUtils.copyProperties(saved,retVal);
        }
        catch (Exception e) {
            System.out.println("service error ok");
            e.printStackTrace();
            throw new GenericCustomErrorException("Unexpected Error");
        }
        return retVal;
    }


    @Override
    public ProductCategoryDTO updateProductCategory(Long categoryId, String categoryName, String pAid) {
        ProductCategoryDTO retVal = new ProductCategoryDTO();
        //cek target categorynya ada
        Optional<ProductCategoryEntity> targetCategory = catRepo.findById(categoryId);
        if(targetCategory.isEmpty())
            throw new NullPointerException("Category not found");
        //cek target categorynya ada
        System.out.println("pAids = "+pAid);
        ClientAddressEntity targetLocation = addressRepo.findByPubId(pAid);
        if(targetLocation==null)
            throw new NullPointerException("Location not found");
        ClientAddressEntity location = addressRepo.findByPubId(pAid);
        Optional<ProductCategoryEntity> existedCategory = catRepo.findByNameIgnoreCaseAndClientAddress_Id(categoryName, location.getId());
        if(existedCategory.isPresent())
            throw new DuplicationErrorException(messageSource.getMessage("label.manager.setting.product.category", null, Locale.getDefault())+": "+categoryName+", "+messageSource.getMessage("msg.isUsed", null, Locale.getDefault()));
        ProductCategoryEntity savedCategory=targetCategory.get();
        savedCategory.setName(categoryName);
        ProductCategoryEntity updatedCategory = catRepo.save(savedCategory);
        retVal = modelMapper.map(updatedCategory,ProductCategoryDTO.class);
        return retVal;
    }

    @Override
    public List<ProductCategoryDTO> getCategoryAndSubCategoryByClientAddressPubId(String pAid) {
        List<ProductCategoryDTO> retVal = new ArrayList<>();
        ClientAddressEntity location = addressRepo.findByPubId(pAid);
        System.out.println("client adddress id == "+location.getId());
        //List<ProductCategoryEntity> daftar = catRepo.findAllByClientAddressSorted(location.getId());
        List<ProductCategoryEntity> daftar = catRepo.findAllByClientAddressHierarchical(location.getId());
        ModelMapper modelMapper = new ModelMapper();
        long indentLevel=0;
        ProductCategoryEntity prevEntity = null;
        List<Long> listCategory = new ArrayList<>();
        for(int i=0;i<daftar.size();i++) {
        //for (ProductCategoryEntity entity : daftar) {
            // mapping otomatis dari entity ke DTO
            ProductCategoryEntity currEntity = new ProductCategoryEntity();

            ProductCategoryDTO dto = new ProductCategoryDTO();
            currEntity = daftar.get(i);
            dto = modelMapper.map(currEntity, ProductCategoryDTO.class);
            System.out.println("PROCESS="+dto.getId()+"-> list size = "+listCategory.size());

            if(i==0) {
                //first
                //add this top category
                listCategory.add(daftar.get(i).getId());
                dto.setIndentLevel((long) listCategory.size());
                System.out.println(dto.getId()+" indent "+listCategory.size());
                prevEntity = daftar.get(i);
            }
            else {

                if(currEntity.getParent()!=null) {
                    //current entity punya parent,
                    //then cek apakah previous parentnya
                    if(currEntity.getParent().getId()==prevEntity.getId()) {
                        //prev == parent
                        //add this sub category
                        listCategory.add(daftar.get(i).getId());
                        System.out.println(dto.getId()+" indent "+listCategory.size());
                        dto.setIndentLevel((long) listCategory.size());
                    }
                    else {
                        //bukan parentnya,
                        //cari dimana parentya di listCategory
                        boolean match = false;
                        for(int j=1;j<= listCategory.size();j++) {
                            System.out.println("listCategory.get(j-1)="+listCategory.get(j-1)+" vs "+currEntity.getParent().getId());
                            if(listCategory.get(j-1)== currEntity.getParent().getId()) {
                                match=true;
                                //ketemu posisi parent, then
                                //masukan current ke list
                                if(j== listCategory.size()) {
                                    //last record, tinggal tambah aja
                                    listCategory.add(daftar.get(i).getId());
                                    dto.setIndentLevel((long) listCategory.size());
                                }
                                else {
                                    //replace current value
                                    listCategory.set(j,daftar.get(i).getId());
                                    dto.setIndentLevel((long) j);
                                }

                                System.out.println(dto.getId()+" indent "+dto.getIndentLevel());

                            }
                            if(match) {
                                //hapus dari belakang yg bukan daftar.get(i).getId()

                                for(int k=listCategory.size()-1;k>0;k--) {
                                    if(listCategory.get(k)==daftar.get(i).getId()) {
                                        break;
                                    }
                                    else {
                                        listCategory.remove(k);
                                    }

                                }
                            }
                        }

                    }
                }
                else {
                    //current = top category
                    listCategory = new ArrayList<>();
                    listCategory.add(daftar.get(i).getId());
                    System.out.println(dto.getId()+" indent "+listCategory.size());
                    dto.setIndentLevel((long) listCategory.size());
                }
                prevEntity = daftar.get(i-1);
            }
            System.out.println("========");
            System.out.println(dto.getName()+"="+dto.getIndentLevel());
            System.out.println("========");
            retVal.add(dto);
        }
        return retVal;
    }


}
