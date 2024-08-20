package com.cloth.business.configurations.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cloth.business.entities.User;
import com.cloth.business.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



public class CustomUserDetails implements UserDetails {


	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public CustomUserDetails(User user) {
		this.user = user;
	}
	
	public User getLoggedInUser() {
		return this.user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<UserRole> roles = user.getRoles();

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for(UserRole role: roles) {
			if(role.getIsActive()) {				
				authorities.add(new SimpleGrantedAuthority(role.getRole()));
			}
		}
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {		
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !user.getIsLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public Boolean logoutRequired() {
		return user.getLogoutRequired();
	}

}
