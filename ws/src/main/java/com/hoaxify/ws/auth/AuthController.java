package com.hoaxify.ws.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.shared.Views;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;

@RestController
public class AuthController {
	
	@Autowired
	UserRepository userRepository;
	
	//spring security ile birlikte password encoder'a bu sinifta ihtiyac kalmadi
	//PasswordEncoder passwordEncode = new BCryptPasswordEncoder();
	
	@PostMapping("/api/auth")
	@JsonView(Views.Base.class) //cevap donerken 'Views.Base.class' ile işaretlenmis alanlari don diyoruz. Ornegin password donmesin cevap olarak
	ResponseEntity<?> handleAuthentication(@CurrentUser User user) { //bizim yazdigimiz anotasyon ile object cast islemi yapilacak
		
		//artik header bilgisi spring security'den gelecegi icin asagidaki if kosuluna gerek yok
		/*
		 * if (authorization == null) { ApiError apiError = new ApiError(401,
		 * "Unauthorized request", "/api/auth"); return
		 * ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError); }
		 */
		
		//user bilgisini artik spring security sayesinde 'authentication' nesnesinden alabiliriz. Asagidaki kodlar iptal
		/*
		//authorization header kismindaki encode'lanmis degeri alacagiz ve bizim tablomuzda boyle bir kullanici var mi diye kontrol edecegiz
		//authorization header'dan donen deger ornegin: 'Basic OnRlc3Q=' seklinde. Bunu split edip ikinci kisimdaki degeri aliyoruz
		String base64encoded = authorization.split("Basic ")[1]; // string'i 'Basic 'ten bol ve elimizdeki iki parcalik array'in ikinci parcasini al dedik
		//simdi elimizdeki degeri decode edelim. (OlA0c3N3b3Jk -> User1:p4ssword) 
		String decoded = new String(Base64.getDecoder().decode(base64encoded));
		String[] parts = decoded.split(":");
		String username = parts[0];
		System.out.println("AuthController.java:username:" + username);
		String password = parts[1];
		System.out.println("AuthController.java:password: " + password);
		//bu kullanici adi ile kayit var mi
		User inDb = userRepository.findByKullaniciAdi(username);
		*/
		
		//artik request'in fail olmasi durumunda spring security devreye girecegi icin ve bu kisimlardan once spring security'den donecegi icin asagidaki kodlara gerek yok
		
		/*
		 * //eger kayit yoksa hata don if(inDb == null) { ApiError apiError = new
		 * ApiError(401, "Unauthorized request", "/api/auth"); return
		 * ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError); } String
		 * hashedPassword = inDb.getPassword(); //parola kontrolu
		 * if(!passwordEncode.matches(password, hashedPassword)) { ApiError apiError =
		 * new ApiError(401, "Unauthorized request", "/api/auth"); return
		 * ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError); }
		 */
		
		return ResponseEntity.ok(user); 
	}
	
	//**** asagidaki kode gerek kalmadi cunku 'errorHandler.java' ile standart hale getirdik
	
	/*
	 * //bu sinifin dahil oldugu islemlerde firlatilan hatalari yakala ve
	 * anlamlandir diyoruz
	 * 
	 * @ExceptionHandler(BadCredentialsException.class) //'BadCredentialsException'
	 * exception'lari yakala //cevap olarak unauthorized don
	 * 
	 * @ResponseStatus(HttpStatus.UNAUTHORIZED) ApiError
	 * handleBadCredentialExceptions() { ApiError apiError = new ApiError(401,
	 * "Unauthorized request", "/api/auth"); return apiError; }
	 */
}
