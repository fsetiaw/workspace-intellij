package com.divillafajar.app.pos.pos_app_sini.service.product.category;

import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.exception.GenericCustomErrorException;
import com.divillafajar.app.pos.pos_app_sini.exception.category.CategoryHasSubCategoryException;
import com.divillafajar.app.pos.pos_app_sini.io.projection.CategoryHierarchyProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.projection.CategorySummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductCategoryHierarchyProjection;
import com.divillafajar.app.pos.pos_app_sini.model.product.CategorySearchResultModel;
import com.divillafajar.app.pos.pos_app_sini.model.product.ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientAddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.product.category.ProductCategoryRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService{

    private final ProductCategoryRepo catRepo;
    private final ClientAddressRepo addressRepo;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public ProductCategoryDTO addNewProductCategory(String categoryName, String pAid) throws Exception {
        ProductCategoryDTO retVal = new ProductCategoryDTO();
        ClientAddressEntity targetLocation = addressRepo.findByPubId(pAid);
        if(targetLocation==null) {
            throw new NullPointerException("Location not found");
        }
        Optional<ProductCategoryEntity> existed = catRepo.findByNameIgnoreCaseAndClientAddress_Id(categoryName, targetLocation.getId());
        if(existed.isPresent()) {
            throw new DuplicationErrorException(messageSource.getMessage("label.manager.setting.product.category", null, LocaleContextHolder.getLocale())+": "+categoryName+", "+messageSource.getMessage("msg.isUsed", null, LocaleContextHolder.getLocale()));
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
            throw new DuplicationErrorException(messageSource.getMessage("label.manager.setting.product.category", null, LocaleContextHolder.getLocale())+": "+categoryName+", "+messageSource.getMessage("msg.isUsed", null, LocaleContextHolder.getLocale()));

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
            CategoryHierarchyProjectionDTO dto =catRepo.findCategoryHierarchyLevelById(saved.getId());
            retVal.setIndentLevel(dto.getLevel());
        }
        catch (Exception e) {
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
        ClientAddressEntity targetLocation = addressRepo.findByPubId(pAid);
        if(targetLocation==null)
            throw new NullPointerException("Location not found");
        ClientAddressEntity location = addressRepo.findByPubId(pAid);
        Optional<ProductCategoryEntity> existedCategory = catRepo.findByNameIgnoreCaseAndClientAddress_Id(categoryName, location.getId());
        if(existedCategory.isPresent()) {
			//jika existed categoryid  ==  categoryId dibolehkan karena mungkin mo betulin huruf besar
	        //jika bukan item itu sendiri throw error sudah ada
	        if (!existedCategory.get().getId().equals(categoryId))
		        throw new DuplicationErrorException(messageSource.getMessage("label.manager.setting.product.category", null, LocaleContextHolder.getLocale())+": "+categoryName+", "+messageSource.getMessage("msg.isUsed", null, LocaleContextHolder.getLocale()));
        }

        ProductCategoryEntity savedCategory=targetCategory.get();
        savedCategory.setName(categoryName);
        ProductCategoryEntity updatedCategory = catRepo.save(savedCategory);
        retVal = modelMapper.map(updatedCategory,ProductCategoryDTO.class);
        return retVal;
    }

	@Override
	public void deleteCategory(Long catId) {
		//cek apakah catId ini memiliki sub category
		List<ProductCategoryEntity> listSubCat = catRepo.findByParentId(catId);
		if(listSubCat!=null && listSubCat.size()>0)
			throw new CategoryHasSubCategoryException(messageSource.getMessage("err.categoryHasSubCategoryException", null, LocaleContextHolder.getLocale()));
		//lanjut hapus
		catRepo.deleteById(catId);
	}

	@Override
	public List<CategorySearchResultModel> searchCategory(String pAid, String kword) {
		List<CategorySearchResultModel> retVal = new ArrayList<>();
		ClientAddressEntity targetLoc = addressRepo.findByPubId(pAid);
		retVal = catRepo.searchCategoryWithPath(targetLoc.getId(),kword);
		return retVal;
	}

	@Override
    public List<ProductCategoryDTO> getCategoryAndSubCategoryByClientAddressPubId(String pAid) {
        List<ProductCategoryDTO> retVal = new ArrayList<>();
        ClientAddressEntity location = addressRepo.findByPubId(pAid);
        //List<ProductCategoryEntity> daftar = catRepo.findAllByClientAddressSorted(location.getId());
		try {
			List<ProductCategoryHierarchyProjection> daftar = catRepo.findAllByClientAddressHierarchical(location.getId());
			ModelMapper modelMapper = new ModelMapper();
			retVal = modelMapper.map(
					daftar,
					new TypeToken<List<ProductCategoryDTO>>() {}.getType()
			);
		}
		catch(Exception e) {
			System.out.println("ADA ERROR");
			e.printStackTrace();
		}
        return retVal;
    }

    @Override
    public List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> getPathToEachEndChildCategoryByClientAddressPubId(String pAid) {

        List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> returnValue = new ArrayList<>();
        ClientAddressEntity location = addressRepo.findByPubId(pAid);
        try {
            returnValue = catRepo.findAllPathEndCategoryChildHierarchical(location.getId());
            /*
            if(pathAndTotItem!=null) {
                for(ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId singleton : pathAndTotItem) {
                    retVal.add(singleton.getFullPath());
                }
            }

             */
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    @Override
    public List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> getPathToEachEndChildCategoryByClientAddressPubId(String pAid, String filter) {

        List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> returnValue = new ArrayList<>();
        ClientAddressEntity location = addressRepo.findByPubId(pAid);
        try {
            if(filter.equalsIgnoreCase("noFilter"))
                returnValue = getPathToEachEndChildCategoryByClientAddressPubId(pAid);
            if(filter.equalsIgnoreCase("emptyCat"))
                returnValue = catRepo.findAllPathEndCategoryChildHierarchicalHasNoItemOnly(location.getId());
            if(filter.equalsIgnoreCase("noEmptyCat"))
                returnValue = catRepo.findAllPathEndCategoryChildHierarchicalHasItemOnly(location.getId());
            if(filter.equalsIgnoreCase("noImage"))
                returnValue = catRepo.findAllPathEndCategoryChildHierarchicalHasItemOnlyButNoImage(location.getId());
            if(filter.equalsIgnoreCase("noPrice"))
                returnValue = catRepo.findAllPathEndCategoryChildHierarchicalHasItemOnlyButNoPrice(location.getId());
            if(filter.equalsIgnoreCase("noDesc"))
                returnValue = catRepo.findAllPathEndCategoryChildHierarchicalHasItemOnlyButNoDesc(location.getId());

            /*
            if(pathAndTotItem!=null) {
                for(ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId singleton : pathAndTotItem) {
                    retVal.add(singleton.getFullPath());
                }
            }

             */
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return returnValue;
    }

	@Override
	public boolean locationHasCategoryProduct(String pAid) {
		boolean retVal=false;
		retVal = catRepo.isCategoryExistAtClientAddressByPubId(pAid);
		return retVal;
	}

    @Override
    public boolean locationHasItemProduct(String pAid) {
        boolean retVal=false;
        ClientAddressEntity clientAddress = addressRepo.findByPubId(pAid);
        retVal = catRepo.isItemExistsByClientAddressId(clientAddress.getId());
        return retVal;
    }

    @Transactional
	@Override
	public void createDefaultCategory(String lang, String clientAddressPubId) {
        ClientAddressEntity targetAddress = addressRepo.findByPubId(clientAddressPubId);
        long cAid = targetAddress.getId();

		if(lang.equalsIgnoreCase("id")) {
            //insert top cat
            catRepo.insertDefaultIdCategories(cAid);
            //insert sub
            catRepo.insertDefaultIdSubFoodCategories(cAid);
            catRepo.insertDefaultIdSubBeverageCategories(cAid);
            catRepo.insertDefaultIdSubComboCategories(cAid);
            catRepo.insertDefaultIdSubDessertCategories(cAid);
            catRepo.insertDefaultIdSubAddOnsCategories(cAid);
            catRepo.insertDefaultIdSubMerchandiseCategories(cAid);

		} else if(lang.equalsIgnoreCase("en")) {
            //insert top cat
            catRepo.insertDefaultEnCategories(cAid);
            //insert sub
            catRepo.insertDefaultEnSubFoodCategories(cAid);
            catRepo.insertDefaultEnSubBeverageCategories(cAid);
            catRepo.insertDefaultEnSubComboCategories(cAid);
            catRepo.insertDefaultEnSubDessertCategories(cAid);
            catRepo.insertDefaultEnSubAddOnsCategories(cAid);
            catRepo.insertDefaultEnSubMerchandiseCategories(cAid);
        }
        //set sudah useDefaultCategory
        targetAddress.setUsedDefaultCategory(true);
        addressRepo.save(targetAddress);
	}

    @Transactional
    public void forceDeleteAllCategoriesByClientAddressId(Long clientAddressId) {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.update("DELETE FROM product_category WHERE client_address_id = ?", clientAddressId);
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

	@Transactional
	public void deleteAllCategoryWithoutProductOnlyByClientAddressId(Long clientAddressId) {
		// Nonaktifkan sementara foreign key check (opsional, tapi tetap aman)
		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

		// Hapus hanya kategori milik clientAddressId tertentu yang
		// TIDAK dipakai oleh produk mana pun
		String sql = """
        WITH RECURSIVE category_with_product AS (
            -- Ambil kategori yang punya produk
            SELECT DISTINCT p.category_id AS id
            FROM product p
            WHERE p.client_address_id = ?
              AND p.deleted = FALSE
              AND p.category_id IS NOT NULL

            UNION ALL

            -- Ambil semua parent dari kategori yang punya produk
            SELECT pc.parent_id AS id
            FROM product_category pc
            INNER JOIN category_with_product cwp ON pc.id = cwp.id
            WHERE pc.parent_id IS NOT NULL
        )
        DELETE FROM product_category
        WHERE client_address_id = ?
          AND id NOT IN (SELECT id FROM category_with_product)
    """;

		jdbcTemplate.update(sql, clientAddressId, clientAddressId);

		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
	}


	@Transactional
    @Override
    public void resetCategoryByClientAddress(String clientAddressPubId) {
        ClientAddressEntity targetAddress = addressRepo.findByPubId(clientAddressPubId);
        long cAid = targetAddress.getId();
		deleteAllCategoryWithoutProductOnlyByClientAddressId(cAid);
        //reset sudah useDefaultCategory
        targetAddress.setUsedDefaultCategory(false);
        addressRepo.save(targetAddress);
    }

    @Override
    public CategorySummaryProjection getSummaryProductCategory(String clientAddressPubId) {
        ClientAddressEntity targetAddress = addressRepo.findByPubId(clientAddressPubId);
        long cAid = targetAddress.getId();
        CategorySummaryProjection retVal = null;
        retVal = catRepo.getCategorySummaryByClientAddressId(cAid);
        return retVal;
    }

}
