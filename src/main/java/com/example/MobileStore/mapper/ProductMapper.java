package com.example.MobileStore.mapper;

import com.example.MobileStore.dto.ProductRequestDTO;
import com.example.MobileStore.dto.ProductResponseDTO;
import com.example.MobileStore.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {ProductMapperHelper.class})
public interface ProductMapper {

        // Chuyển đổi từ Product (entity) sang ProductResponseDTO
        @Mapping(target = "imageIds", source = "images", qualifiedByName = "mapToImageIds")
        ProductResponseDTO toDTO(Product product);

        // Chuyển đổi từ ProductRequestDTO sang Product (entity)
        @Mapping(target = "images", source = "imageIds", qualifiedByName = "mapToProductImages")
        @Mapping(source = "categoryID", target = "category", qualifiedByName = "mapCategory")
        @Mapping(source = "manufacturerID", target = "manufacturer", qualifiedByName = "mapManufacturer")
        @Mapping(source = "conditionID", target = "condition", qualifiedByName = "mapCondition")
        @Mapping(target = "id", ignore = true) // ID sẽ tự động tạo
        Product toEntity(ProductRequestDTO requestDTO);

}

