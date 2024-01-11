package com.phamanh.accountservice.security;

import com.phamanh.accountservice.domains.Account;
import com.phamanh.accountservice.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;



/*
 * để làm việc với JWT thì cần phải đưa user đăng nhập vào bên trong hệ thống để được quản lí bởi managerSpringSucirity
 * để làm việc đó cần có 2 lớp  : 
 * lớp 1 là UserPrinciple dùng để build User với hệ thống thông qua các hàm có sẵn và hàm build
 * lớp 2 là kiểm tra User có username đăng nhập vào có tồn tại trong database không,nếu có sẽ gọi đến hàm build của lớp UserPrinciple để đưa vào hệ thống quản lí
 * việc so khớp password đăng nhập và password của User trong database có khớp hay không sẽ được tự động thông qua manager
 * cả 2 lớp trên đều đã được quy định bắt buộc bởi spring sucurity
 */
@Service
public class UserDetailService implements UserDetailsService {
	
	@Autowired
    AccountRepository accountRepository;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("username not found -> username or password"+username));
		
		return UserPrinciple.build(account);
	}
	//HAM LAY RA USER HIEN TAI DE THUC HIEN THAO TAC VOI DB


}
