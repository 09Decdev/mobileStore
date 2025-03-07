package com.example.MobileStore.service;

import com.example.MobileStore.dto.ProductRequestDTO;
import com.example.MobileStore.dto.ProductResponseDTO;
import com.example.MobileStore.entity.Product;
import com.example.MobileStore.entity.ProductImage;
import com.example.MobileStore.mapper.ProductMapper;
import com.example.MobileStore.repository.ImageRepository;
import com.example.MobileStore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductMapper productMapper;

    public ProductService(ProductRepository repository, ImageRepository imageRepository, ProductMapper productMapper) {
        this.repository = repository;
        this.imageRepository = imageRepository;
        this.productMapper = productMapper;
    }

public List<ProductResponseDTO> getAllProduct(){
    return repository.findAll()
            .stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
}
public  List<ProductResponseDTO>getProductByName(String name){
    return repository.findByNameContainingIgnoreCase(name)
            .stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
}

@Transactional
public ProductResponseDTO createProductWithImages(ProductRequestDTO requestDTO,List<MultipartFile>files)throws IOException{
    Product product=productMapper.toEntity(requestDTO);
    List<ProductImage>images=uploadProductImage(files);
    images.forEach(image->image.setProduct(product));
    product.setImages(images);

    Product saveProduct=repository.save(product);
    return productMapper.toDTO(saveProduct);
}

public List<ProductImage> uploadProductImage(List<MultipartFile>files) throws IOException {
    String uploadDir="uploads/products/";
    File dir=new File(uploadDir);
    if (!dir.exists()) dir.mkdirs();

    List<ProductImage>images=new ArrayList<>();

    for (MultipartFile file:files){
        if (file.isEmpty())continue;

        String filename= UUID.randomUUID()+ "_" + file.getOriginalFilename();
        Path filePath= Paths.get(uploadDir + filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        ProductImage image=new ProductImage();
        image.setImageUrl(uploadDir+filename);
        images.add(image);
    }
    return images;
}
      public  void deleteProduct(Long id){
        Product product=repository.findById(id).orElseThrow(()->new RuntimeException("Not Found Product With Id: "+id));
        repository.delete(product);
    }

    public  ProductResponseDTO getProductById(Long id){
        return repository.findById(id).map(productMapper::toDTO)
                .orElseThrow(()-> new RuntimeException("Not Found Product With ID: "+id));
    }

    public UrlResource loadProductImage(Long productId, Long imageId) throws IOException {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        if (!product.getImages().contains(image)) {
            throw new RuntimeException("Image not found in product");
        }

        Path imagePath = Paths.get(image.getImageUrl());
        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("Image file not found");
        }

        return new UrlResource(imagePath.toUri());
    }

    public String getImageContentType(Resource resource)throws IOException{
        Path imagePath=Paths.get(resource.getFile().getAbsolutePath());
        String contentType=Files.probeContentType(imagePath);
        return contentType != null ?contentType:"application/octet-stream";
    }

    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO, List<MultipartFile>files)throws  IOException{
        Product product=repository.findById(productId).orElseThrow(()->new RuntimeException("Not found product"));

        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());
        product.setDescription(productRequestDTO.getDescription());

        if (files!=null&&!files.isEmpty()){
            product.getImages().clear();
            List<ProductImage> newImage=uploadProductImage(files);
            newImage.forEach(image->{image.setProduct(product);
                product.getImages().add(image);
            });
//            product.setImages(newImage);
        }
        Product productUpdate= repository.save(product);
        return productMapper.toDTO(productUpdate);
    }
}
