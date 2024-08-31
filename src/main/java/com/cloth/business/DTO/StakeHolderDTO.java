package com.cloth.business.DTO;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.cloth.business.entities.Store;
import com.cloth.business.entities.enums.StakeHolderType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StakeHolderDTO {

    private Long id;

    @NotNull(message = "Stakeholder type is required")
    private StakeHolderType stakeHolderType;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

    @NotBlank(message = "Phone is required")
    @Size(max = 15, message = "Phone cannot be longer than 15 characters")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot be longer than 255 characters")
    private String address;

    private String email;

    private String image;

    private Boolean isActive;

    private Date createdAt;

    private Date updatedAt;

    private MultipartFile stakeHolderImage;
    
    private Store store;

    @JsonIgnore
    public MultipartFile getStakeHolderImage() {
        return stakeHolderImage;
    }

    @JsonProperty
    public void setStakeHolderImage(MultipartFile stakeHolderImage) {
        this.stakeHolderImage = stakeHolderImage;
    }
}
