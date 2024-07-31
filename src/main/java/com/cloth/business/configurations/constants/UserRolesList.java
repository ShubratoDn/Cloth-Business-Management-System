package com.cloth.business.configurations.constants;

import java.util.ArrayList;
import java.util.List;

import com.cloth.business.entities.UserRole;

public class UserRolesList {

	public static List<UserRole> userRoles = new ArrayList<>();
	

    static {
        userRoles.add(createRole(null, "ROLE_USER_CREATE", "User Create", "user", true));
        userRoles.add(createRole(null, "ROLE_USER_DELETE", "User Delete", "user", true));
        userRoles.add(createRole(null, "ROLE_USER_UPDATE", "User Update", "user", true));
        userRoles.add(createRole(null, "ROLE_USER_GET", "User Get", "user", true));

        userRoles.add(createRole(null, "ROLE_STORE_CREATE", "Store Create", "store", true));
        userRoles.add(createRole(null, "ROLE_STORE_UPDATE", "Store Update", "store", true));
        userRoles.add(createRole(null, "ROLE_STORE_DELETE", "Store Delete", "store", true));
        userRoles.add(createRole(null, "ROLE_STORE_GET", "Store Get", "store", true));
    }
	
	private static UserRole createRole(Long id, String role, String title, String category, Boolean isActive ) {
		UserRole userRole = new UserRole();
		
		userRole.setId(id);
		userRole.setRole(role);
		userRole.setTitle(title);
		userRole.setCategory(category);
		userRole.setIsActive(isActive);
		
		return userRole;
	}
	
}
