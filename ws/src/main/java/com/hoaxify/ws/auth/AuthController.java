package com.hoaxify.ws.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;
import com.hoaxify.ws.user.vm.UserVM;

@RestController
public class AuthController {
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/api/auth")
	UserVM handleAuthentication(@CurrentUser User user) { //bizim yazdigimiz anotasyon ile object cast islemi yapilacak
		return new UserVM(user);
	}
}
