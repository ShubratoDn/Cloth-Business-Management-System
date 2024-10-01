package com.cloth.business.helpers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cloth.business.configurations.security.CustomUserDetails;
import com.cloth.business.entities.User;
import com.cloth.business.entities.UserRole;
import com.cloth.business.payloads.PageResponse;

public class HelperUtils {

	public static PageResponse pageToPageResponse(Page<?> pageInfo) {
		PageResponse pageData = new PageResponse();
		pageData.setContent(pageInfo.getContent());
		pageData.setPage(pageInfo.getNumber());
		pageData.setSize(pageInfo.getSize());
		pageData.setTotalElements(pageInfo.getTotalElements());
		pageData.setTotalPages(pageInfo.getTotalPages());
		pageData.setNumberOfElements(pageInfo.getNumberOfElements());

		pageData.setEmpty(pageInfo.isEmpty());
		pageData.setFirst(pageInfo.isFirst());
		pageData.setLast(pageInfo.isLast());
		return pageData;
	}
	
	

	public static User getLoggedinUser() {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User loggedInUser = customUserDetails.getLoggedInUser();
		return loggedInUser;
	}
	
	
	public static boolean userHasRole(String roleName) {
		CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User loggedInUser = customUserDetails.getLoggedInUser();
		List<UserRole> roles = loggedInUser.getRoles();
		boolean flag = false;
		
		for(UserRole role: roles) {
			if(role.getRole().equalsIgnoreCase(roleName)) {
				flag=true;
				break;
			}
		}
		return flag;		
	}
}
