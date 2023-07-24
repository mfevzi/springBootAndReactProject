package com.hoaxify.ws.user;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//anotasyonumuzun ismi ve hangi field icin kullanacaksak o field'in tipi. Biz 'String kullaniciAdi' icin kullanacagiz
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>{
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public boolean isValid(String kullaniciAdi, ConstraintValidatorContext context) {
		User user = userRepository.findByKullaniciAdi(kullaniciAdi);
		if(user != null) {
			return false;
		}
		return true;
	} 
}
