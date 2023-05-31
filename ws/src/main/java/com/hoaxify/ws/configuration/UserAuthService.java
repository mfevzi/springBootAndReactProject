package com.hoaxify.ws.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;

@Service
//user authentication islemlerinde username ve password degerlerinin ayiklanmasi icin olusturulmus siniftir
public class UserAuthService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User inDb = userRepository.findByKullaniciAdi(username);
		if(inDb == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return inDb;
	}
	
}
