package com.example.MobileStore.service;

import com.example.MobileStore.entity.Product;
import com.example.MobileStore.entity.ProductImage;
import com.example.MobileStore.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository repository;

//
//    public void saveImages(Product product, List<MultipartFile> imageFiles) {
//        for (MultipartFile file : imageFiles) {
//            try {
//                ProductImage image = new ProductImage();
//                image.setProduct(product);
//                image.setImageData(file.getBytes());
//                image.setName(file.getOriginalFilename());
//                image.setType(file.getContentType());
//                repository.save(image);
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to store image", e);
//            }
//        }
//    }
}

