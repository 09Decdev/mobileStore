package com.example.MobileStore.mapper;

import com.example.MobileStore.dto.ProductRequestDTO;
import com.example.MobileStore.dto.ProductResponseDTO;
import com.example.MobileStore.entity.Product;
import com.example.MobileStore.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface productMapper {
    @Mapping(target = "productImages",source = "imageUrl",qualifiedByName ="mapImagesToUrls")
    ProductResponseDTO toDTO(Product product);
    @Named("mapImagesToUrls")
    static List<String>mapImagesToUrls(List<ProductImage>images){
        if (images==null)return null;
        return images.stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true) // ID sẽ tự động sinh
    @Mapping(target = "manufacturer", ignore = true) // Được set từ Service
    @Mapping(target = "category", ignore = true) // Được set từ Service
    @Mapping(target = "condition", ignore = true) // Được set từ Service
    @Mapping(source = "imageUrls", target = "productImages", qualifiedByName = "mapUrlsToImages")
     Product toEntity(ProductRequestDTO requestDTO);
    @Named("mapUrlsToImages")
    static List<ProductImage>mapUrlsToImages(List<String>imageURL){
        if (imageURL==null)return null;
        return imageURL.stream()
                .map(url->{
                    ProductImage image=new ProductImage();
                    image.setImageUrl(url);
                    return image;}
        ).collect(Collectors.toList());
    }

}
