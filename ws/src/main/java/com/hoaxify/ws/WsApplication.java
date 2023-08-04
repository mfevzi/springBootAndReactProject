package com.hoaxify.ws;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.hoaxify.ws.hoax.Hoax;
import com.hoaxify.ws.hoax.HoaxService;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserService;

//spring securtiy butun end pointleri secure etmesin diye asagidaki haric tutma ozelligini uyguladik
//daha sonrasinda 'SecurityConfiguration' sinifini kullanima soktuk ve exlude ettigimiz kod satirini iptal ettik
@SpringBootApplication/*(exclude = SecurityAutoConfiguration.class)*/
public class WsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsApplication.class, args);
	}
	
	//asagidaki metot uygulama ayaga kalkarken otomatik olarak calisacaktir
	//'CommandLineRunner' class'i ozel bir classtir. Biz uygulama baslatilirken kullanici olustursun istiyoruz
	@Bean //bu anotasyon ile bu metodu bir spring context'i yapmis oluyoruz ve artik spring @bean anotasyonuna sagip CommandLineRunner tipindeki metodu gorunce uygulamayi baslatirken bu metodu cagiracak
	@Profile("dev") //bu metot sadece dev profilinde (application.yaml'deki prfillerden biri) calissin
	CommandLineRunner createInitialUsers(UserService userService, HoaxService hoaxService) {
		System.out.println("@bean anotasyonuna sahip createInitialUsers metodu cagrildi");
		/*
		 * return new CommandLineRunner() {
		 * 
		 * @Override public void run(String... args) throws Exception { User user = new
		 * User(); user.setKullaniciAdi("User1"); user.setDisplayName("Display1");
		 * user.setPassword("P4ssword"); userService.save(user); } };
		 */
		
		//yukaridaki kodu asagidaki sekilde arrow function olarak yazabiliriz
		//bir interface'nin sadece bir tane metodu varsa arrow function kullanabiliriz
		//'CommandLineRunner' class'inin run metodunu lambda function olarak kullandik
		return (args) -> {
			for(int i = 1; i < 31; i++) {
				User user = new User();
				user.setKullaniciAdi("User"+i);
				user.setDisplayName("Display"+i);
				user.setPassword("P4ssword");
				userService.save(user);
				//her kullanici baslangicta iki tane hoaxa sahip olsun
				for(int j = 1; j < 3; j++) {
					Hoax hoax = new Hoax();
					hoax.setContent("Hoax " + j + " from user " + i);
					hoaxService.save(hoax, user);
				}
			}
		};
	}
}
