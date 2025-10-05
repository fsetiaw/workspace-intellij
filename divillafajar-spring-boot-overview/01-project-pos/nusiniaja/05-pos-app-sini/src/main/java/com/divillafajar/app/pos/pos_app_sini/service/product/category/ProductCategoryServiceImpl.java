package com.divillafajar.app.pos.pos_app_sini.service.product.category;

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

    @Override
    public ProductCategoryDTO addNewProductCategory(String categoryName, String pAid) {
        ProductCategoryDTO retVal = new ProductCategoryDTO();

        ClientAddressEntity targetLocation = addressRepo.findByPubId(pAid);
        if(targetLocation==null)
            throw new NullPointerException("Location not found");

        Optional<ProductCategoryEntity> existed = catRepo.findByNameIgnoreCaseAndClientAddress_Id(categoryName, targetLocation.getId());
        if(existed.isPresent())
            throw new DuplicationErrorException("Category Sudah Ada");

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
    public ProductCategoryDTO updateProductCategory(Long categoryId, String categoryName, String pAid) {
        ProductCategoryDTO retVal = new ProductCategoryDTO();
        Optional<ProductCategoryEntity> targetCategory = catRepo.findById(categoryId);
        if(targetCategory.isEmpty())
            throw new NullPointerException("Category not found");
        ProductCategoryEntity savedCategory=targetCategory.get();
        savedCategory.setName(categoryName);
        ProductCategoryEntity updatedCategory = catRepo.save(savedCategory);
        retVal = modelMapper.map(updatedCategory,ProductCategoryDTO.class);
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
            throw new DuplicationErrorException("Category Sudah Ada");

        Optional<ProductCategoryEntity> parentEntity = catRepo.findById(parentId);
        if(parentEntity.isEmpty())
            throw new NullPointerException("Parent entity not found");

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
    public List<ProductCategoryDTO> getCategoryAndSubCategoryByClientAddressPubId(String pAid) {
        List<ProductCategoryDTO> retVal = new ArrayList<>();
        ClientAddressEntity location = addressRepo.findByPubId(pAid);
        System.out.println("client adddress id == "+location.getId());
        //List<ProductCategoryEntity> daftar = catRepo.findAllByClientAddressSorted(location.getId());
        List<ProductCategoryEntity> daftar = catRepo.findAllByClientAddressHierarchical(location.getId());
        ModelMapper modelMapper = new ModelMapper();
        long indentLevel=0;
        ProductCategoryEntity prevEntity = null;
        for(int i=0;i<daftar.size();i++) {
        //for (ProductCategoryEntity entity : daftar) {
            // mapping otomatis dari entity ke DTO
            ProductCategoryEntity currEntity = new ProductCategoryEntity();

            ProductCategoryDTO dto = new ProductCategoryDTO();
            currEntity = daftar.get(i);
            dto = modelMapper.map(currEntity, ProductCategoryDTO.class);
            if(i==0) {
                //first
                dto.setIndentLevel(indentLevel);
                prevEntity = daftar.get(i);
            }
            else {

                if(currEntity.getParent()!=null) {
                    //cek apakah previous parentnya
                    if(currEntity.getParent().getId()==prevEntity.getId()) {
                        //prev == parent
                        dto.setIndentLevel(++indentLevel);
                    }
                    else {
                        //bukan parent,
                        //cek apa prev punya parrent && parennta apa sama
                        if(prevEntity.getParent()!=null
                            && currEntity.getParent().getId()==prevEntity.getParent().getId()) {
                            //cek apa parennya sama
                            dto.setIndentLevel(indentLevel);
                        }
                        else {
                            //prev ngga punya parent
                            dto.setIndentLevel(++indentLevel);
                        }

                    }
                }
                else {
                    //current = top category
                    indentLevel=0;
                    dto.setIndentLevel(indentLevel);
                }
                prevEntity = daftar.get(i-1);
            }
            retVal.add(dto);
        }
        return retVal;
    }


}
