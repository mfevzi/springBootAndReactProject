package com.hoaxify.ws.auth;

import com.hoaxify.ws.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * kendimiz bir token entity'si olusturuyoruz
 * @author mehmetfevziakdeniz
 *
 */
@Entity
@Data
public class Token {
	
	// token'lar zaten unique olacagindan bu alani primary key yapabiliriz
	// bunu biz generate edecegimizden generate anotasyonu eklemedik
	@Id
	private String token;
	// bir kullanicinin birden fazla token'i olabilecegi bir iliski olacak
	@ManyToOne
	private User user;
}
