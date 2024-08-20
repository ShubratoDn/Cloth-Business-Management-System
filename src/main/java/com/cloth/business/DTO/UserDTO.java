package com.cloth.business.DTO;

import com.cloth.business.entities.Store;
import com.cloth.business.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDTO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    @NotBlank(message= "Name can not be empty")
    @Size(min = 3, message = "Name should contain minimum 3 characters")
    private String name;

    @NotBlank(message = "Phone can not be empty")
    @Size(min = 10, max = 15, message = "Phone number should be between 10 and 15 digits")
    private String phone;

    private String email;
    private String address;
    private String password;
    private String remark;
    private String designation;
    private Boolean isLocked;
    private Date createdAt;
    private Date updatedAt;
    private Boolean logoutRequired;
    
    @NotNull(message = "Image is required")
    private MultipartFile userImage;
    
    private String image;
    
    private List<UserRole> roles = new ArrayList<>();
    
    private List<Store> ownedStore = new ArrayList<>();
    
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    @JsonIgnore
    public MultipartFile getUserImage() {
        return this.userImage;
    }

    @JsonProperty
    public void setUserImage(MultipartFile userImage) {
        this.userImage = userImage;
    }
    
    
    @JsonIgnore
    public Boolean getLogoutRequired() {
        return this.logoutRequired;
    }

    @JsonProperty
    public void setLogoutRequired(Boolean logoutRequired) {
        this.logoutRequired = logoutRequired;
    }
    
}
