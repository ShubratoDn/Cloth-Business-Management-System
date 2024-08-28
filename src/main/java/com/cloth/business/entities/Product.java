package com.cloth.business.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import org.springframework.web.multipart.MultipartFile;

@Data
@Entity
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotBlank(message = "Size is required")
    private String size;

    private String image;

    private String remark;

    @Transient
    private MultipartFile productImage;

    @JsonIgnore
    public MultipartFile getProductImage() {
        return productImage;
    }

    @JsonProperty
    public void setProductImage(MultipartFile productImage) {
        this.productImage = productImage;
    }
    
    
    @Transient
    private String productCategory;
}
