package com.example.MobileStore.controller;

import com.example.MobileStore.dto.ProductRequestDTO;
import com.example.MobileStore.dto.ProductResponseDTO;
import com.example.MobileStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/create", consumes ="multipart/form-data" )//MediaType.MULTIPART_FORM_DATA_VALUE
    public ResponseEntity<ProductResponseDTO>createProductWithImage(
            @ModelAttribute ProductRequestDTO productRequestDTO,
            @RequestPart("images") List<MultipartFile> images){

        try {
            ProductResponseDTO productResponseDTO=productService.createProductWithImages(productRequestDTO,images);
            return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProduct(){
        List<ProductResponseDTO> productResponseDTOS=productService.getAllProduct();
        return ResponseEntity.ok(productResponseDTOS);
    }

    @GetMapping("/search")
    public List<ProductResponseDTO>searchProducts(@RequestParam String name){
        return productService.getProductByName(name);
    }
    @GetMapping("/{id}")
    public ProductResponseDTO getProductByID(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product with id: "+id+"has been deleted successfully");
    }

    @PutMapping(value = "/{id}",consumes = "multipart/form-data")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable("id")Long id,
                                                     @ModelAttribute ProductRequestDTO productRequestDTO,
                                                     @RequestPart("images") List<MultipartFile> images){
        try {
            ProductResponseDTO productResponseDTO= productService.updateProduct(id,productRequestDTO,images);
            return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
