package com.hoaxify.ws.hoax;

import java.util.Date;

import com.hoaxify.ws.file.FileAttachment;
import com.hoaxify.ws.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
	
	//string turundeki bir sutun default olarak 255 karaktere kadar izin verir. Bunu 1000'e cikaralim
	@Column(length = 1000)
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP) //temporal anotasyonu ile tarih bilgisinin formatini degistirebiliriz
	private Date timestamp;
	
	@ManyToOne
	private User user;
	
	// fileAttachment entity'sinde de oneToOne seklinde hoax ile iliskilendirme yapildigindan..
	// ..burada diyoruz ki hoax tablosunda iliskiyi belirten bir alan olmasin iliski fileAttachment tablosundaki..
	//..hoax field'i uzerinden yurusun. Bu sekilde cift yonlu bir iliski kurduk ama hoax tablosuna yeni alan..
	//..acmadik. Artik hoax'tan fileAttachment'a da ulasabiliriz
	@OneToOne(mappedBy = "hoax")
	private FileAttachment fileAttachment;
}
