package com.hoaxify.ws.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	//@Autowired
	//dependency injection islemini bu anotasyon haricinde constructor metot ile de yapabiliriz. Ornegi asagida 
	UserRepository userRepository;
	
	//spring security kullanarak password sifreleme yapacagiz
	PasswordEncoder passwordEncoder;
	
	//@Autowired eger sinif icinde sadece bir tane constructor varsa 'AutoWired' anotasyonuna ihtiyac duyulmaz. Koysak da olur
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public void save(User user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword())); //bu sekilde encode edilmis bir string tekrar decode edilemez
		userRepository.save(user);
	}

}
