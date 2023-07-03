package com.hoaxify.ws.user;

import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.user.vm.UserUpdateVM;
import com.hoaxify.ws.user.vm.UserVM;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api") //post-get mapping adreslerimizin basina bu ifadeyi eklemis oluyoruz. asagida tekrara gerek yok
public class UserController {
	
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired //dependency injection kullanmis oluyoruz. Sinifi bagimli oldugu nesneden bagimsiz hale getiriyoruz
	UserService userService;
	
	//react projemizdeki 'package.json' dosyasinda yaptigimiz proxy ayari sayesinde crossorigin'a ihtiyacimiz kalmadi
	//@CrossOrigin //react'in calistigi port ile spring boot'un calistigi port farkli oldugunda bu farkliliga takilmadan isler yurusun diye
	//@RequestBody annotasyonu ile POST veya PUT request'leri handle edilir. Genelde JSON veya XML formatında bir request'i nesneye dönüştürmek için kullanılır.
	@PostMapping("/users")
	//@Valid ile, gelen requestin entity bazinda valid olmasi gerektigini soyluyoruz. Orada tanimlanan sekle uygun olmali
	public GenericResponse/*ResponseEntity<?>*/ createUser(@Valid @RequestBody User user) { //string turundeki json verisini benim istedigim nesne turunde ver diyoruz. Bunu 'Jackson' kutuphanesi sayesinde yapiyor
		userService.save(user);
		log.info(user.toString());
		//return ResponseEntity.ok(new GenericResponse("User created")); 
		return new GenericResponse("User created");
	}
	
	@GetMapping("/users")
	//spring data'nin pageable objesini kullaniyoruz. Verileri sayfa sayfa alabilmek icin
	//login olan kullanici bilgisini '@CurrentUser' ile alabiliyoruz
	public Page<UserVM> getirTumKullanicilar(Pageable page, @CurrentUser User user){//page nesnesi default parametrelere sahiptir ve kullanici girdisine gore de calisabilir
		//user objemizi userVM'ye donusturmek icin 'map' metodunu kullanacagiz
		//map icindeki fonksiyon user alsin ve userVM donsun
		//burada java 8 ile birlikte gelen METOT REFERANS ozelligini (map(UserVM::new)) kullanalim
		//user tipinde donen her bir nesne map metodu araciligi ile userVM'in constructor metodune gonderilir ve oradan..
		//..donen userVM tipindeki nesneyi dondurur
		return userService.getirTumKullanicilar(page, user).map(UserVM::new);
		
	}
	
	//sadece bir kullanicinin donduruldugu get metodu olusturalim
	//bu metotda 'username' ifadesi kullanicinin unique bir ozelligidir ve degisken olarak alinir. Bu yuzden suslu parantez icinde
	@GetMapping("/users/{username}")
	//path tarafindan gelecek string tipindeki username degiskenini metodumuzda kullanmak icin '@PathVariable' anotasyonunu kullaniyoruz
	//yukaridaki path'de isimlendirmeyi nasil yaptiysak asagida da o sekilde kullaniyoruz
	public UserVM getUser(@PathVariable String username) {
		User user = userService.getByUsername(username);
		return new UserVM(user);
	}
	
	//var olan bir kullanicinin displayName bilgisini guncellemek icin metot yazalim
	//guncelleme islemi icin 'putMapping' kullaniyoruz
	//hangi kullanicinin displayName'ini guncelleyecegimize iliskin bilgiye ihtiyacimiz oldugundan kullanicinin unique degerini de (username) alalim
	//@RequestBody annotasyonu ile POST veya PUT request'leri handle edilir. Genelde JSON veya XML formatında bir request'i nesneye dönüştürmek için kullanılır.
	@PutMapping("/users/{username}")
	//bu istegi atmaya kullanicinin yetkisi var mi diye kontrol ediyoruz. "updateUser" metodu calismadan..
	// once (@PreAuthorize) calisacak. Bu anotasyon icine yazilan ifade 'Spring Expression Language (SpEL)'
	//SpEL sayesinde hash (#) ifadesi ile metot parametrelerine erisebiliriz
	//'username' degeri 'loggedInUser' nesnesinin 'kullaniciAdi' alanina esit degilse spring security 403 forbidden firlatir
	//@Valid ile 'UserUpdateVM' tipindeki nesnenin kurallara uygun olmasini sagliyoruz. Degilse hata aliriz
	@PreAuthorize("#username == #loggedInUser.kullaniciAdi")
	public UserVM updateUser(@PathVariable String username, @Valid @RequestBody UserUpdateVM updatedUser, @CurrentUser User loggedInUser) {
		User user = userService.updateUser(username, updatedUser);
		return new UserVM(user);
	}
}
