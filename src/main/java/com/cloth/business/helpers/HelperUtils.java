package com.cloth.business.helpers;

import java.util.List;

import com.cloth.business.entities.Store;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cloth.business.configurations.security.CustomUserDetails;
import com.cloth.business.entities.User;
import com.cloth.business.entities.UserRole;
import com.cloth.business.payloads.PageResponse;

import jakarta.servlet.http.HttpServletRequest;

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


	// Method to check if a user has any of the provided stores assigned.
//	public static boolean userAssignedThisStore(List<Store> stores) {
//		// Fetch the currently logged-in user's assigned stores
//		List<Store> loggedInUsersAssignedStore = getLoggedinUser().getAssignedStore();
//
//		// Check if any of the provided stores match the user's assigned stores
//		for (Store store : stores) {
//			if (loggedInUsersAssignedStore.contains(store)) {
//				return true; // If a match is found, return true
//			}
//		}
//
//		return false; // If no match, return false
//	}


	public static boolean userAssignedThisStore(Store store) {
		// Fetch the currently logged-in user's assigned stores
		List<Store> loggedInUsersAssignedStore = getLoggedinUser().getAssignedStore();

		// Check if any of the provided stores match the user's assigned stores
		for (Store userStore : loggedInUsersAssignedStore) {
			if (userStore.getId() == store.getId()) {
				return true; // If a match is found, return true
			}
		}

		return false; // If no match, return false
	}
	
	
	public static String getBaseURL() {
        // Get the current HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null; // or throw an exception if needed
        }

        HttpServletRequest request = attributes.getRequest();
        String scheme = request.getScheme(); // http or https
        String serverName = request.getServerName(); // localhost or domain
        int serverPort = request.getServerPort(); // port number
        String contextPath = request.getContextPath(); // context path

        // Construct the base URL
        String baseUrl = scheme + "://" + serverName + ":" + serverPort + contextPath;

        return baseUrl;
    }
}
