package com.hoaxify.ws.hoax;

import java.util.Date;

import com.hoaxify.ws.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Hoax {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Size(min = 1, max = 1000)
	//string turundeki bir sutun default olarak 255 karaktere kadar izin verir. Bunu 1000'e cikaralim
	@Column(length = 1000)
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP) //temporal anotasyonu ile tarih bilgisinin formatini degistirebiliriz
	private Date timestamp;
	
	@ManyToOne
	private User user;
}
