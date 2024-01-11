package com.phamanh.accountservice.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phamanh.accountservice.domains.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrinciple implements UserDetails {

	private Long id;

	private String username;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> role;
	
	public static UserPrinciple build(Account account) {
		List<GrantedAuthority> authorities = account.getRoles().stream().map(role->
			new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
			
			return new UserPrinciple(
					account.getId(),
					account.getUsername(),
					account.getPassword(),
					authorities
					);
				
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
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

}
