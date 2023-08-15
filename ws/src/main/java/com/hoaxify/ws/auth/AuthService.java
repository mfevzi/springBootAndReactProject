package com.hoaxify.ws.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;
import com.hoaxify.ws.user.UserService;
import com.hoaxify.ws.user.vm.UserVM;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
	
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	TokenRepository tokenRepository;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenRepository = tokenRepository;
	}

	public AuthResponse authenticate(Credentials credentials) {
		// oncelikle credential objesi icindeki username ile kullanicinin varligini kontrol edelim
		User inDb = userRepository.findByKullaniciAdi(credentials.getUsername());
		// kullanici yoksa yetkisiz exception'i firlatalim
		if(inDb == null) {
			throw new AuthException();
		}
		// credential ile gelen password ile user'in db'deki password'u esit mi diye kontrol edelim
		boolean matches = passwordEncoder.matches(credentials.getPassword(), inDb.getPassword());
		// eslesme yoksa yetkisiz exception'i firlatalim
		if(!matches) {
			throw new AuthException();
		}
		// eger eslesiyorsa 'AuthResponse' donelim. Bunun icin userVM ve token'a ihtiyac var
		UserVM userVM = new UserVM(inDb);
		String token = generateRandomToken();
		// token'i db'ye kaydedelim
		Token tokenEntity = new Token();
		tokenEntity.setToken(token);
		tokenEntity.setUser(inDb);
		tokenRepository.save(tokenEntity);
		
		AuthResponse response = new AuthResponse();
		response.setUser(userVM);
		response.setToken(token);
		return response;
	}
	
	@Transactional // buraya bir request geldiginde transaction baslatilsin ve..
	// ..request sonlanana kadar transaction acik kalsin
	public UserDetails getUserDetails(String token) {
		Optional<Token> optionalToken = tokenRepository.findById(token);
		// eger boyle bir token yoksa null donelim
		if(!optionalToken.isPresent()) {
			return null;
		}
		// token mevcutsa ilgili token'in user'ini donelim
		return optionalToken.get().getUser();
		
		/*
		// token'i parse edip icinden user id'yi bulalim. Hatirla, token..
		// ..uretirken icine user id koyuyorduk
		JwtParser parser = Jwts.parser().setSigningKey("my-app-secret");
		// exception firlatilmiyorsa o zaman token valid'tir
		try {
			parser.parse(token);
			Claims claims = parser.parseClaimsJws(token).getBody();
			// claims icinden de verdigimiz subject'i (user id) alabiliyoruz
			long userId = Long.parseLong(claims.getSubject());
			Optional<User> userOptional = userRepository.findById(userId);
			User user = userOptional.get();
			return user;
		} catch (Exception e) {
			System.out.println("Err: " + e);
		}
		return null;
		*/
	}
	
	// kendimiz token uretelim
	public String generateRandomToken() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
