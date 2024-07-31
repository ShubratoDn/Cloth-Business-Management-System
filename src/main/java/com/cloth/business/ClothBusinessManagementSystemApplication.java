package com.cloth.business;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import com.cloth.business.configurations.constants.UserRolesList;
import com.cloth.business.entities.UserRole;
import com.cloth.business.exceptions.ResourceAlreadyExistsException;
import com.cloth.business.repositories.UserRoleRepository;
import com.cloth.business.services.UserRoleService;

import io.lettuce.core.models.role.RedisInstance.Role;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableCaching	
@Slf4j
public class ClothBusinessManagementSystemApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClothBusinessManagementSystemApplication.class, args);
    }

    @Bean
    ModelMapper mapper(){
        return  new ModelMapper();
    }

    
    @Autowired
    private UserRoleRepository userRoleRepository;
    

    @Override
    public void run(String... args) throws Exception {
    	List<UserRole> userRoles = UserRolesList.userRoles;
    	saveAllRoles(userRoles);
    }

//    @CacheEvict(value = "userRoles", key = "#userRoles")
    
	private List<UserRole> saveAllRoles(List<UserRole> userRoles) {
		List<UserRole> roleList = new ArrayList<>();
		
		for(UserRole role: userRoles) {
			UserRole dbRole = userRoleRepository.findByRole(role.getRole());
	        if(dbRole != null){
	        	log.warn("The role:{} is already exist",role.getRole());
	            continue;
	        }else {
	        	UserRole save = userRoleRepository.save(role);
	        	roleList.add(save);
	        }
		}
		
		return roleList;
		
	}

}
