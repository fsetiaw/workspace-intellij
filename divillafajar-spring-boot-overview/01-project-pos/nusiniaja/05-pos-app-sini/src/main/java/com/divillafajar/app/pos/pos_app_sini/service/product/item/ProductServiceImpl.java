package com.divillafajar.app.pos.pos_app_sini.service.product.item;

import com.divillafajar.app.pos.pos_app_sini.common.enums.ProductStatusEnum;
import com.divillafajar.app.pos.pos_app_sini.exception.DuplicationErrorException;
import com.divillafajar.app.pos.pos_app_sini.exception.GenericCustomErrorException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.category.ProductCategoryEntity;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductItemSummaryProjectionDTO;
import com.divillafajar.app.pos.pos_app_sini.io.projection.ProductWithCategoryPathDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientAddressDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductEntity;
import com.divillafajar.app.pos.pos_app_sini.model.item.CreateItemRequestModel;
import com.divillafajar.app.pos.pos_app_sini.model.item.UpdateItemRequestModel;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientAddressRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.product.ProductRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.product.category.ProductCategoryRepo;
import com.divillafajar.app.pos.pos_app_sini.service.image.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

	private final ProductRepo productRepo;
	private final ProductCategoryRepo categoryRepo;
	private final ClientAddressRepo addressRepo;
	private final ModelMapper modelMapper;
    private final ImageStorageService imageService;


    //@Value("${app.upload.image-dir}")
    //private String imageBaseDir;

	@Override
	@Transactional
	public ProductDTO addNewProduct(Long catId, ClientAddressDTO dto, CreateItemRequestModel createItemRequestModel) {
		ProductDTO retVal = new ProductDTO();
		System.out.println("===== addNewProduct =======");
		System.out.println("===== "+dto.getPubId()+" =======");

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
		return retVal;
	}

    @Override
    public ProductDTO updateProduct(Long itemId, UpdateItemRequestModel updateItemRequestModel) {
        ProductDTO retVal = new ProductDTO();
        Optional<ProductEntity> targetEntity = productRepo.findById(itemId);
        if(targetEntity.isEmpty())
            throw new NullPointerException("item not found");

        targetEntity.get().setName(updateItemRequestModel.getName());
        targetEntity.get().setDescription(updateItemRequestModel.getDescription());
        targetEntity.get().setPrice(BigDecimal.valueOf(updateItemRequestModel.getPrice()));
        ProductEntity updatedProduct = productRepo.save(targetEntity.get());
        retVal = modelMapper.map(updatedProduct, ProductDTO.class);
        return retVal;
    }


    @Override
	public List<ProductWithCategoryPathDTO> getListProduct(String clietnAddressPubId, Long categoryId, String keyword) {
		System.out.println("==============getListProduct==================");
		System.out.println("clietnAddressPubId="+clietnAddressPubId);
		System.out.println("categoryId="+categoryId);
		List<ProductWithCategoryPathDTO> retVal = new ArrayList<>();
        List<ProductWithCategoryPathDTO> listItem = null;
        if(keyword==null || keyword.isEmpty()) {
            listItem = productRepo.findProductsWithCategoryPathByClientAndCategory(clietnAddressPubId, categoryId);
        }
        else {
            listItem = productRepo.findProductsWithCategoryPathByClientAddressAndCategoryFilterByKwordProductName(clietnAddressPubId, categoryId, keyword);
        }

		if(listItem!=null) {
			System.out.println("listItem="+listItem.size());
			retVal = listItem;
		}
		return retVal;
	}

	@Override
	public ProductItemSummaryProjectionDTO getSummaryProductItem(String clientAddressPubId) {
		ProductItemSummaryProjectionDTO retVal = null;
		ClientAddressEntity clientAddress = addressRepo.findByPubId(clientAddressPubId);
		retVal = productRepo.getProductItemSummaryByClientAddressId(clientAddress.getId());
		return retVal;
	}
}
