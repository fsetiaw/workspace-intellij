package com.divillafajar.app.pos.pos_app_sini.service.area;

import com.divillafajar.app.pos.pos_app_sini.common.enums.LineOfBusinessEnum;
import com.divillafajar.app.pos.pos_app_sini.common.enums.ProductStatusEnum;
import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.exception.GenericCustomErrorException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.SpaceAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.dto.SpaceAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.unit.AreaUnitEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.unit.dto.UnitAccomodationDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.CategoryHierarchyProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductCategoryHierarchyProjection;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaHierarchyProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaSummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.SpaceAreaHierarchyProjection;
import com.divillafajar.app.pos.pos_app_sini.model.area.unit.CreateUnitAreaRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.item.CreateItemRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.product.ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserSessionDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.area.AreaRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.area.UnitAreaRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientAddressRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService{
	private final AreaRepo areaRepo;
	private final UnitAreaRepo unitAreaRepo;
	private final ClientAddressRepo addressRepo;
	private final MessageSource messageSource;
    private final ModelMapper modelMapper;
	private final JdbcTemplate jdbcTemplate;

	@Override
	public boolean locationHasArea(String pAid) {
		Long result = areaRepo.hasAreaByClientAddressPubId(pAid);
		return result != null && result > 0;
	}

	@Override
	public AreaSummaryProjection getAreaSummary(String clientAddressPubId) {
		AreaSummaryProjection retVal= null;
		retVal = areaRepo.getSpaceAreaSummaryByClientAddressPubId(clientAddressPubId);
		return retVal;
	}

	@Override
	@Transactional
	public ProductCategoryDTO addNewMainArea(String areaName, String pAid, String username) throws Exception {
		ProductCategoryDTO retVal = new ProductCategoryDTO();
		ClientAddressEntity targetLocation = addressRepo.findByPubId(pAid);
		if(targetLocation==null) {
			throw new NullPointerException("Location not found");
		}
		//Optional<ProductCategoryEntity> existed = areaRepo.findByNameIgnoreCaseAndClientAddress_Id(areaName, targetLocation.getId());

		if(areaRepo.existsByNameInAnyArea(pAid,areaName)==1) {
			throw new DuplicationErrorException(messageSource.getMessage("label.areaName", null, LocaleContextHolder.getLocale())+": "+areaName+", "+messageSource.getMessage("msg.isUsed", null, LocaleContextHolder.getLocale()));
		}
		SpaceAreaEntity newArea = new SpaceAreaEntity();
		newArea.setName(areaName);
		newArea.setClientAddress(targetLocation);
		newArea.setIndentLevel(0L);
		newArea.setCreatedBy(username);
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attr != null) {
			HttpSession session = attr.getRequest().getSession(false); // false = jangan buat baru
			if (session != null) {
				UserSessionDTO userLog = (UserSessionDTO)session.getAttribute("userLogInfo");
				newArea.setCreatedBy(userLog.getUsername());
				System.out.println("UserLog dari session: " + userLog);
				// ... pakai data userLog di sini
			}
		}// false = jangan buat baru

		try {
			SpaceAreaEntity saved = areaRepo.save(newArea);
			BeanUtils.copyProperties(saved,retVal);
		}
		catch (Exception e) {
			throw new GenericCustomErrorException("Unexpected Error");
		}
		return retVal;
	}

	@Override
	public List<SpaceAreaDTO> getAreaAndSubAreaByClientAddressPubId(String pAid) {
		List<SpaceAreaDTO> retVal = new ArrayList<>();
		ClientAddressEntity location = addressRepo.findByPubId(pAid);
		//List<ProductCategoryEntity> daftar = areaRepo.findAllByClientAddressSorted(location.getId());
		try {
			System.out.println("getAreaAndSubAreaByClientAddressPubId == "+location.getId());
			List<SpaceAreaHierarchyProjection> daftar = areaRepo.findAllByClientAddressHierarchical(location.getId());
			ModelMapper modelMapper = new ModelMapper();
			retVal = modelMapper.map(
					daftar,
					new TypeToken<List<SpaceAreaDTO>>() {}.getType()
			);
		}
		catch(Exception e) {
			System.out.println("ADA ERROR");
			e.printStackTrace();
		}
		return retVal;
	}

    @Override
    @Transactional
    public SpaceAreaDTO addSubMainArea(Long parentId, String areaName, String pAid, String username) {
        System.out.println("ADDsUBmAINaREA");
        SpaceAreaDTO retVal = new SpaceAreaDTO();
        ClientAddressEntity targetLocation = addressRepo.findByPubId(pAid);
        if(targetLocation==null)
            throw new NullPointerException("Location not found");
        if(areaRepo.existsByNameIgnoreCaseAndClientAddress_IdAndDeletedFalse(areaName, targetLocation.getId()))
            throw new DuplicationErrorException(messageSource.getMessage("areaName", null, LocaleContextHolder.getLocale())+": "+areaName+", "+messageSource.getMessage("msg.isUsed", null, LocaleContextHolder.getLocale()));

        Optional<SpaceAreaEntity> parentEntity = areaRepo.findById(parentId);
        if(parentEntity.isEmpty())
            throw new NullPointerException("Parent area Not Found");
        SpaceAreaEntity newArea = new SpaceAreaEntity();
        newArea.setName(areaName.trim());
        newArea.setClientAddress(targetLocation);
        newArea.setParent(parentEntity.get());
		newArea.setCreatedBy(username);
        try {
            SpaceAreaEntity saved = areaRepo.save(newArea);
            modelMapper.getConfiguration()
                    .setSkipNullEnabled(true);
            modelMapper.map(saved,retVal);
            AreaHierarchyProjectionDTO dto =areaRepo.findCategoryHierarchyLevelById(saved.getId());
            retVal.setIndentLevel(dto.getLevel());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new GenericCustomErrorException("Unexpected Error");
        }
        return retVal;
    }

    @Override
    public List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> getPathToEachEndChildCategoryByClientAddressPubId(String pAid) {

        List<ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId> returnValue = new ArrayList<>();
        ClientAddressEntity location = addressRepo.findByPubId(pAid);
        try {
            returnValue = areaRepo.findAllPathEndCategoryChildHierarchical(location.getId());
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
                returnValue = areaRepo.findAllPathEndCategoryChildHierarchicalHasNoItemOnly(location.getId());
            if(filter.equalsIgnoreCase("noEmptyCat"))
                returnValue = areaRepo.findAllPathEndCategoryChildHierarchicalHasItemOnly(location.getId());
            if(filter.equalsIgnoreCase("noImage"))
                returnValue = areaRepo.findAllPathEndCategoryChildHierarchicalHasItemOnlyButNoImage(location.getId());
            if(filter.equalsIgnoreCase("noPrice"))
                returnValue = areaRepo.findAllPathEndCategoryChildHierarchicalHasItemOnlyButNoPrice(location.getId());
            if(filter.equalsIgnoreCase("noDesc"))
                returnValue = areaRepo.findAllPathEndCategoryChildHierarchicalHasItemOnlyButNoDesc(location.getId());

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
	public void createDefaultArea(String lang, String clientAddressPubId) {
		ClientAddressEntity targetAddress = addressRepo.findByPubId(clientAddressPubId);
		long cAid = targetAddress.getId();

		if(targetAddress.getLocationCategory().equalsIgnoreCase(LineOfBusinessEnum.ACCOMODATION.name())) {
			if(lang.equalsIgnoreCase("id")) {
				//insert top cat
				areaRepo.insertDefaultIdArea(cAid);
				//insert sub

				areaRepo.insertDefaultIdSubAccomodation(cAid);
				areaRepo.insertDefaultIdSubPublicArea(cAid);
				areaRepo.insertDefaultIdSubSecurity(cAid);
				areaRepo.insertDefaultIdSubFB(cAid);
				areaRepo.insertDefaultIdSubPark(cAid);
				/*

				areaRepo.insertDefaultIdSubAddOnsCategories(cAid);
				areaRepo.insertDefaultIdSubMerchandiseCategories(cAid);
				*/


			} else if(lang.equalsIgnoreCase("en")) {
				/*insert top cat
				areaRepo.insertDefaultEnCategories(cAid);
				//insert sub
				areaRepo.insertDefaultEnSubFoodCategories(cAid);
				areaRepo.insertDefaultEnSubBeverageCategories(cAid);
				areaRepo.insertDefaultEnSubComboCategories(cAid);
				areaRepo.insertDefaultEnSubDessertCategories(cAid);
				areaRepo.insertDefaultEnSubAddOnsCategories(cAid);
				areaRepo.insertDefaultEnSubMerchandiseCategories(cAid);

				 */
			}
			//set sudah useDefaultCategory
			targetAddress.setUsedDefaultArea(true);

		}
		else if(targetAddress.getLocationCategory().equalsIgnoreCase(LineOfBusinessEnum.FOODBAVERAGES.name())) {

			System.out.println("!!! HARUS UPDATE DI AreaServiceImpl.createDefault dulu !!!!!");
		}
		addressRepo.save(targetAddress);
	}

	@Transactional
	@Override
	public void resetAreaByClientAddress(String clientAddressPubId) {
		ClientAddressEntity targetAddress = addressRepo.findByPubId(clientAddressPubId);
		long cAid = targetAddress.getId();
		softDeleteAllAreaWithoutProductOnlyByClientAddressId(cAid);
		//reset sudah useDefaultCategory
		targetAddress.setUsedDefaultArea(false);
		addressRepo.save(targetAddress);
	}

	@Transactional
	public void softDeleteAllAreaWithoutProductOnlyByClientAddressId(Long clientAddressId) {

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

        UPDATE product_category
        SET deleted = TRUE
        WHERE client_address_id = ?
          AND deleted = FALSE     -- hanya yang belum soft delete
          AND id NOT IN (SELECT id FROM category_with_product)
    """;

		jdbcTemplate.update(sql, clientAddressId, clientAddressId);
	}

	@Override
	@Transactional
	public ProductDTO addNewUnit(Long areaId, ClientAddressDTO dto, CreateUnitAreaRequestModel createItemRequestModel, String username) {

		ProductDTO retVal = new ProductDTO();
		System.out.println("===== addNewProduct =======");
		System.out.println("===== "+dto.getPubId()+" =======");
		/*
		cek nama tipe sudah digunakan
		 */
		if(unitAreaRepo.existsByName(createItemRequestModel.getName()))
			throw new DuplicationErrorException("");

		try {
			AreaUnitEntity newUnit = new AreaUnitEntity();
			newUnit =  modelMapper.map(createItemRequestModel, AreaUnitEntity.class);

			newUnit.setCreatedBy(username);
			Optional<SpaceAreaEntity> area =  areaRepo.findById(areaId);
			if(area.isEmpty())
				throw new NullPointerException("Area Not Found");

			newUnit.setSpaceArea(area.get());
			newUnit.setAreaType(newUnit.getSpaceArea().getName());
			/*
			System.out.println("newUnit = "+newUnit.getSpaceArea().getName());
			System.out.println("newUnit = "+newUnit.getName());
			System.out.println("newUnit = "+newUnit.getCreatedBy());
			System.out.println("newUnit = "+newUnit.getFeatureFacilities().getFirst());
			System.out.println("newUnit = "+newUnit.getGeneralFacilities().getFirst());
			System.out.println("newUnit = "+newUnit.getOnDemandFacilities().getFirst());
			System.out.println("newUnit = "+newUnit.getOtherFeature());

			 */
			unitAreaRepo.save(newUnit);
		} catch (Exception e) {
			e.printStackTrace();
		}


/*
		//check apa nama sudah digunakan di lokasi tersebut
		ProductEntity exist =  productRepo.searchProductsByClientAddressPubIdAndName(dto.getPubId(), createItemRequestModel.getName());
		if(exist!=null) {
			Optional<ProductCategoryEntity> pce = categoryRepo.findById(exist.getCategory().getId());
			System.out.println("--"+pce.get().getName());
			throw new DuplicationErrorException(pce.get().getName());
		}

		Optional<ProductCategoryEntity> categoryEntity = categoryRepo.findById(catId);
		if(categoryEntity.isEmpty())
			throw new NullPointerException("category Not Found");

		try {

			ProductEntity newItem = new ProductEntity();
			newItem.setName(createItemRequestModel.getName());
			newItem.setStatus(ProductStatusEnum.DRAFT);
			newItem.setCategory(categoryEntity.get());
			newItem.setDescription(createItemRequestModel.getDescription());
			System.out.println("pit1");
			ClientAddressEntity targetLocatiopn = addressRepo.findByPubId(dto.getPubId());
			System.out.println("pit2");
			newItem.setClientAddress(targetLocatiopn);
			System.out.println("pit3");
			ProductEntity savedProduct = productRepo.save(newItem);
			System.out.println("pit4");
			retVal = modelMapper.map(savedProduct,ProductDTO.class);
			System.out.println("pit5");
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericCustomErrorException(e.getMessage());
		}

 */
		return null;
	}
}
