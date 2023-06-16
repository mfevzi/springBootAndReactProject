package com.hoaxify.ws.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.shared.Views;

import jakarta.validation.Valid;

@RestController
public class UserController {
	
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired //dependency injection kullanmis oluyoruz. Sinifi bagimli oldugu nesneden bagimsiz hale getiriyoruz
	UserService userService;
	
	//react projemizdeki 'package.json' dosyasinda yaptigimiz proxy ayari sayesinde crossorigin'a ihtiyacimiz kalmadi
	//@CrossOrigin //react'in calistigi port ile spring boot'un calistigi port farkli oldugunda bu farkliliga takilmadan isler yurusun diye
	@PostMapping("/api/users")
	//@Valid ile, gelen requestin entity bazinda valid olmasi gerektigini soyluyoruz. Orada tanimlanan sekle uygun olmali
	public GenericResponse/*ResponseEntity<?>*/ createUser(@Valid @RequestBody User user) { //string turundeki json verisini benim istedigim nesne turunde ver diyoruz. Bunu 'Jackson' kutuphanesi sayesinde yapiyor
		/* artik hatalari 'validationHatasiYakala' metodu yakalayacagindan asagidaki kontrollere gerek yok
		ApiError err = new ApiError(400, "validation error", "/api/users");
		Map<String, String> validationErrors = new HashMap<>();
		
		String username = user.getUserName();
		String displayName = user.getDisplayName();
		
		if(username == null || username.isEmpty()) {	
			validationErrors.put("kullaniciAdi", "kullanici adi bos birakilamaz!");
		}
		if(displayName == null || displayName.isEmpty()) {	
			validationErrors.put("displayName", "displayName bos birakilamaz!");
		}
		
		if(validationErrors.size() > 0) {
			err.setValidationErrors(validationErrors);
			//validation hatalarinda bad request donmek gerekir 400 hatasi
			//responseEntity generic bir class'tir ve ne dondugunu belirtmek gerekir
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
		}
		*/
		userService.save(user);
		log.info(user.toString());
		//return ResponseEntity.ok(new GenericResponse("User created")); 
		return new GenericResponse("User created");
	}
	
	@GetMapping("/api/users")
	@JsonView(Views.Base.class) //cevap donerken 'Views.Base.class' ile işaretlenmis alanlari don diyoruz.
	//spring data'nin pageable objesini kullaniyoruz. Verileri sayfa sayfa alabilmek icin
	public Page<User> getirTumKullanicilar(Pageable page){//page nesnesi default parametrelere sahiptir ve kullanici girdisine gore de calisabilir
		return userService.getirTumKullanicilar(page);
	}
	
	//asagidaki kod 'ErrorHandler' sinifinin devreye alinmasi ile iptal edilmistir
	/*
	@ExceptionHandler(MethodArgumentNotValidException.class) //bu sinifin (userController) dahil oldugu bir zincirde 'MethodArgumentNotValidException' hatasi olursa 'validationHatasiYakala' metodunu calistir
	@ResponseStatus(HttpStatus.BAD_REQUEST) //bu metot 'bad request' respons donsun. Aksi durumda 200 doner
	public ApiError validationHatasiYakala(MethodArgumentNotValidException exception) {
		ApiError err = new ApiError(400, "validation error", "/api/users");
		Map<String, String> validationErrors = new HashMap<>();
		//olusabilecek hatalari ve aciklamalarini map'imize koyalim
		for(FieldError fieldError:exception.getBindingResult().getFieldErrors()) {
			validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		err.setValidationErrors(validationErrors);
		return err;
	}
	*/
}
