package com.cloth.business.configurations.constants;

import java.util.ArrayList;
import java.util.List;

import com.cloth.business.entities.UserRole;

public class UserRolesList {

	public static List<UserRole> userRoles = new ArrayList<>();
	
	//Used to save these role to the database automatically
	static {
//	    userRoles.add(createRole(null, "ROLE_ADMIN", "Admin", "admin", true));

		
	    // User Management Roles
	    userRoles.add(createRole(null, "ROLE_USER_CREATE", "User Create", "user", true));
	    userRoles.add(createRole(null, "ROLE_USER_GET", "User Get", "user", true));

	    // Role Management Roles
	    userRoles.add(createRole(null, "ROLE_ROLE_CREATE", "Role Create", "role", true));
	    userRoles.add(createRole(null, "ROLE_ROLE_GET", "Role Get", "role", true));
	    userRoles.add(createRole(null, "ROLE_ROLE_ASSIGN", "Role Assign", "role", true));

	    // Store Management Roles
	    userRoles.add(createRole(null, "ROLE_STORE_CREATE", "Store Create", "store", true));
	    userRoles.add(createRole(null, "ROLE_STORE_GET", "Store Get", "store", true));
	    userRoles.add(createRole(null, "ROLE_STORE_UPDATE", "Store Update", "store", true));
	    userRoles.add(createRole(null, "ROLE_STORE_ASSIGN", "Store Assign", "store", true));

	    // Product Management Roles
	    userRoles.add(createRole(null, "ROLE_PRODUCT_CREATE", "Product Create", "product", true));
	    userRoles.add(createRole(null, "ROLE_PRODUCT_GET", "Product Get", "product", true));
	    
	    // StakeHolder Management Roles
	    userRoles.add(createRole(null, "ROLE_STAKEHOLDER_GET", "Stakeholder Get", "stakeholder", true));
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
