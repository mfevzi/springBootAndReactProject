package com.hoaxify.ws.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;
import com.hoaxify.ws.user.vm.UserVM;

@RestController
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@PostMapping("/api/auth")
	AuthResponse handleAuthentication(@RequestBody Credentials credentials) {
		System.out.println("Calisti...");
		return authService.authenticate(credentials);
	}
	
	// kullanici logout oldugunda calisan metot
	@PostMapping("/api/logout")
	GenericResponse handleLogout(@RequestHeader(name = "Authorization") String authorization) {
		// authorization header'inda token'in basindaki 'baerer ' ifadesini almiyoruz
		String token = authorization.substring(7);
		authService.clearToken(token);
		return new GenericResponse("Logout success");
	}
}
