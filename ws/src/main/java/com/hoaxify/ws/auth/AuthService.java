package com.hoaxify.ws.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserService;
import com.hoaxify.ws.user.vm.UserVM;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthService {
	
	UserService userService;
	PasswordEncoder passwordEncoder;

	public AuthService(UserService userService, PasswordEncoder passwordEncoder) {
		super();
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	public AuthResponse authenticate(Credentials credentials) {
		// oncelikle credential objesi icindeki username ile kullanicinin varligini kontrol edelim
		User inDb = userService.getByUsername(credentials.getUsername());
		// credential ile gelen password ile user'in db'deki password'u esit mi diye kontrol edelim
		boolean matches = passwordEncoder.matches(credentials.getPassword(), inDb.getPassword());
		// eger eslesiyorsa 'AuthResponse' donelim. Bunun icin userVM ve token'a ihtiyac var
		if(matches) {
			UserVM userVM = new UserVM(inDb);
			// 'setSubject' icindeki deger herhangi bir sey olabilir. Biz user id kullandik..
			// ..String deger olsun diye '("" + inDb.getId())' kullanimina gittik.
			// ele alinan degerin hangi algoritmayla ve bizim uygulamamiza ozel imza ile..
			// ..saklanmasi icin 'SignatureAlgorithm.HS512' algoritmasini ve 'my-app-secret' ifadesini kullandik..
			// ..buradaki 'my-app-secret' key'inin sakli tutulmasi gerekir.
			String token = Jwts.builder().setSubject(""+inDb.getId()).signWith(SignatureAlgorithm.HS512, "my-app-secret").compact();
			AuthResponse response = new AuthResponse();
			response.setUser(userVM);
			response.setToken(token);
			return response;
		}
		return null;
	}

}
