package com.example.MobileStore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

//Nhận dữ liệu
public class ProductRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String  description;

    @NotBlank(message = "Price is required")
    @Min(value = 0,message = "Product price must be greater than or equal to 0")
    private double price;

    @NotBlank(message = "Stock is required")
    @Min(value = 0,message = "The amount of inventory must be greater than or equal to 0")
    private int stock;

    @NotBlank(message = "Manufacturer is required")
    private Integer manufacturerID;

    @NotBlank(message = "Category is required")
    private Integer categoryID;

    @NotBlank(message = "Condition ís required")
    private Integer conditionID;

    @NotBlank(message = "Image ís required")
    private List<String>imageUrls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Integer getManufacturerID() {
        return manufacturerID;
    }

    public void setManufacturerID(Integer manufacturerID) {
        this.manufacturerID = manufacturerID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getConditionID() {
        return conditionID;
    }

    public void setConditionID(Integer conditionID) {
        this.conditionID = conditionID;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
