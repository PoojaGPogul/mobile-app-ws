package com.example.app.ws.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.app.ws.io.entity.AuthorityEntity;
import com.example.app.ws.io.entity.RoleEntity;
import com.example.app.ws.io.entity.UserEntity;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = -6665078713568168025L;
	
	private UserEntity userEntity;
	private String userId;

	public UserPrincipal(UserEntity userEntity) {
		this.userEntity = userEntity;
		this.userId = userEntity.getUserId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		List<AuthorityEntity> authorityEntities = new ArrayList<AuthorityEntity>();
		
		//Get user Roles
		Collection<RoleEntity> roles = userEntity.getRoles();
		
		if(roles == null) {
			return grantedAuthorities;
		}
		roles.forEach((role) -> {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
			authorityEntities.addAll(role.getAuthorities());
		});
		
		authorityEntities.forEach((authority) -> {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
		});
		
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return this.userEntity.getEncryptedPassword();
	}

	@Override
	public String getUsername() {
		return this.userEntity.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.userEntity.getEmailVerificationStatus();
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
