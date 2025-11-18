package com.divillafajar.app.pos.pos_app_sini.service.area;

import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.exception.GenericCustomErrorException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.SpaceAreaEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.space.dto.SpaceAreaDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.CategoryHierarchyProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductCategoryHierarchyProjection;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaHierarchyProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.AreaSummaryProjection;
import com.divillafajar.app.pos.pos_app_sini.io.projection.area.SpaceAreaHierarchyProjection;
import com.divillafajar.app.pos.pos_app_sini.model.product.ReturnValueGetPathToEachEndChildCategoryByClientAddressPubId;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserSessionDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.area.AreaRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientAddressRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
	private final ClientAddressRepo addressRepo;
	private final MessageSource messageSource;
    private final ModelMapper modelMapper;

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
	public ProductCategoryDTO addNewMainArea(String areaName, String pAid) throws Exception {
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
    public SpaceAreaDTO addSubMainArea(Long parentId, String areaName, String pAid) {
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
}
