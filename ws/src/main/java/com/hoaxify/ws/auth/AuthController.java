package com.hoaxify.ws.auth;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.Views;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;

@RestController
public class AuthController {
	
	@Autowired
	UserRepository userRepository;
	
	PasswordEncoder passwordEncode = new BCryptPasswordEncoder();
	
	@PostMapping("/api/auth")
	@JsonView(Views.Base.class) //cevap donerken 'Views.Base.class' ile işaretlenmis alanlari don diyoruz. Ornegin password donmesin cevap olarak
	// bu sefer body kismini degil header kismini aliyoruz
	// required = false = bu header olmasa da bu requeste bizim metodumuz ulassin
	ResponseEntity<?> handleAuthentication(
			@RequestHeader(name = "Authorization", required = false) String authorization) {
		if (authorization == null) {
			ApiError apiError = new ApiError(401, "Unauthorized request", "/api/auth");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
		}
		//authorization header kismindaki encode'lanmis degeri alacagiz ve bizim tablomuzda boyle bir kullanici var mi diye kontrol edecegiz
		//authorization header'dan donen deger ornegin: 'Basic OnRlc3Q=' seklinde. Bunu split edip ikinci kisimdaki degeri aliyoruz
		System.out.println("encoded: " + authorization);
		String base64encoded = authorization.split("Basic ")[1]; // string'i 'Basic 'ten bol ve elimizdeki iki parcalik array'in ikinci parcasini al dedik
		//simdi elimizdeki degeri decode edelim. (OlA0c3N3b3Jk -> User1:p4ssword) 
		String decoded = new String(Base64.getDecoder().decode(base64encoded));
		String[] parts = decoded.split(":");
		String username = parts[0];
		System.out.println("username:" + username);
		String password = parts[1];
		System.out.println("password: " + password);
		//bu kullanici adi ile kayit var mi
		User inDb = userRepository.findByKullaniciAdi(username);
		//eger kayit yoksa hata don
		if(inDb == null) {
			ApiError apiError = new ApiError(401, "Unauthorized request", "/api/auth");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
		}
		String hashedPassword = inDb.getPassword();
		//parola kontrolu
		if(!passwordEncode.matches(password, hashedPassword)) {
			ApiError apiError = new ApiError(401, "Unauthorized request", "/api/auth");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
		}
		
		return ResponseEntity.ok(inDb); 
	}
}
