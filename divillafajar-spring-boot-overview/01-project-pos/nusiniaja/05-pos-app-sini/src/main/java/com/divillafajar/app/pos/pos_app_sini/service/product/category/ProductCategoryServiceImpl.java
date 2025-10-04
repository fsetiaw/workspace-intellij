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
        List<ProductCategoryEntity> daftar = catRepo.findAllByClientAddressSorted(location.getId());
        List<ProductCategoryEntity> allCategories =
                catRepo.findAllWithParentAndChildrenByClientAddress(location.getId());

        // Map untuk grouping berdasarkan parent
        Map<Long, List<ProductCategoryEntity>> childrenMap = allCategories.stream()
                .filter(c -> c.getParent() != null)
                .collect(Collectors.groupingBy(c -> c.getParent().getId()));

        // Ambil semua top-level category (tanpa parent), urut alfabetis
        List<ProductCategoryEntity> topCategories = allCategories.stream()
                .filter(c -> c.getParent() == null)
                .sorted(Comparator.comparing(c -> c.getName().toLowerCase()))
                .toList();

        // Final list berurutan (parent → anak → parent berikutnya)
        List<ProductCategoryEntity> sortedHierarchy = new ArrayList<>();

        for (ProductCategoryEntity parent : topCategories) {
            sortedHierarchy.add(parent);

            // Ambil semua anak dari parent, urut alfabet
            List<ProductCategoryEntity> children = childrenMap.get(parent.getId());
            if (children != null) {
                children.sort(Comparator.comparing(c -> c.getName().toLowerCase()));
                sortedHierarchy.addAll(children);
            }
        }
        //ModelMapper modelMapper = new ModelMapper();
        return sortedHierarchy.stream()
                .map(entity -> modelMapper.map(entity, ProductCategoryDTO.class))
                .collect(Collectors.toList());
    }
}
