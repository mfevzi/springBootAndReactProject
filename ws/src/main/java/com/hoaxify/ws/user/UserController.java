package com.hoaxify.ws.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.shared.GenericResponse;

@RestController
public class UserController {
	
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired //dependency injection kullanmis oluyoruz. Sinifi bagimli oldugu nesneden bagimsiz hale getiriyoruz
	UserService userService;
	
	//react projemizdeki 'package.json' dosyasinda yaptigimiz proxy ayari sayesinde crossorigin'a ihtiyacimiz kalmadi
	//@CrossOrigin //react'in calistigi port ile spring boot'un calistigi port farkli oldugunda bu farkliliga takilmadan isler yurusun diye
	@PostMapping("/api/users")
	public GenericResponse createUser(@RequestBody User user) { //string turundeki json verisini benim istedigim nesne turunde ver diyoruz. Bunu 'Jackson' kutuphanesi sayesinde yapiyor
		userService.save(user);
		log.info(user.toString());
		return new GenericResponse("User created");
	}
}
