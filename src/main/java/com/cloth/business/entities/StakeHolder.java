package com.cloth.business.entities;

import java.util.Date;

import com.cloth.business.entities.enums.StakeHolderType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class StakeHolder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private StakeHolderType stakeHolderType;
	
	private String name;
	
	private String phone;
	
	private String email;
	
	private String address;
	
	private String image;
	
	private Boolean isActive;
	
	private Date createdAt;
	
	private Date updatedAt;
	
}
