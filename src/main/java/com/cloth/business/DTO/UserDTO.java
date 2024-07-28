package com.cloth.business.DTO;

import com.cloth.business.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UserDTO {
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

    private List<UserRole> roles = new ArrayList<>();

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
