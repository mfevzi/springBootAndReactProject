package com.hoaxify.ws.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	//verileri sayfa sayfa alabilmek icin spring data'nin pageable nesnesini kullaniyoruz
	public Page<User> getirTumKullanicilar(Pageable page) {
		//veri tabanindan donen listeyi sayfa sayfa almak istersek donus tipi 'pageable' olan metodu kullaniriz
		return userRepository.findAll(page);
	}

}
