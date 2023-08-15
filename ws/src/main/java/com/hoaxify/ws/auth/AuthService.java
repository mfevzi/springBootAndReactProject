package com.hoaxify.ws.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;
import com.hoaxify.ws.user.UserService;
import com.hoaxify.ws.user.vm.UserVM;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import jakarta.transaction.Transactional;

@Service
public class AuthService {
	
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
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
		// 'setSubject' icindeki deger herhangi bir sey olabilir. Biz user id
		// kullandik..
		// ..String deger olsun diye '("" + inDb.getId())' kullanimina gittik.
		// ele alinan degerin hangi algoritmayla ve bizim uygulamamiza ozel imza ile..
		// ..saklanmasi icin 'SignatureAlgorithm.HS512' algoritmasini ve 'my-app-secret'
		// ifadesini kullandik..
		// ..buradaki 'my-app-secret' key'inin sakli tutulmasi gerekir.
		String token = Jwts.builder().setSubject("" + inDb.getId()).signWith(SignatureAlgorithm.HS512, "my-app-secret")
				.compact();
		AuthResponse response = new AuthResponse();
		response.setUser(userVM);
		response.setToken(token);
		return response;
	}
	
	@Transactional // buraya bir request geldiginde transaction baslatilsin ve..
	// ..request sonlanana kadar transaction acik kalsin
	public UserDetails getUserDetails(String token) {
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
	}

}
