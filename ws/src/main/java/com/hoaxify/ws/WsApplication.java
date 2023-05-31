package com.hoaxify.ws;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

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
	CommandLineRunner createInitialUsers(UserService userService) {
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
			User user = new User();
			user.setKullaniciAdi("User1");
			user.setDisplayName("Display1");
			user.setPassword("P4ssword");
			userService.save(user);
		};
	}
}
