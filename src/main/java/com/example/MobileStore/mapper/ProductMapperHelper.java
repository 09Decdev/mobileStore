package com.example.MobileStore.mapper;

import com.example.MobileStore.entity.Category;
import com.example.MobileStore.entity.Conditions;
import com.example.MobileStore.entity.Manufacturer;
import com.example.MobileStore.entity.ProductImage;
import com.example.MobileStore.service.CategoryService;
import com.example.MobileStore.service.ManufacturerService;
import com.example.MobileStore.service.ConditionService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component  //  Thêm @Component để Spring Boot có thể quản lý bean này
public class ProductMapperHelper {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ConditionService conditionService;

    @Named("mapCategory")
    public Category mapCategory(Integer categoryID) {
        return (categoryID == null) ? null : categoryService.findById(categoryID);
    }

    @Named("mapManufacturer")
    public Manufacturer mapManufacturer(Integer manufacturerID) {
        return (manufacturerID == null) ? null : manufacturerService.findById(manufacturerID);
    }

    @Named("mapCondition")
    public Conditions mapCondition(Integer conditionID) {
        return (conditionID == null) ? null : conditionService.findById(conditionID);
    }

//    @Named("mapToImageIds")
//    public List<Long> mapToImageIds(List<ProductImage> images) {
//        if (images==null)return null;
//        return images.stream().map(ProductImage::getId).toList();
//    }
    @Named("mapToImageIds")
    public List<String> mapToImageIds(List<ProductImage> images) {
        if (images==null)return null;
        return images.stream().map(ProductImage::getImageUrl).toList();
    }


    @Named("mapToProductImages")
    public List<ProductImage> mapToProductImage(List<Long>imageIds){
        if (imageIds==null) return null;
        return imageIds.stream().map(id->new ProductImage(id)).collect(Collectors.toList());
    }
}
