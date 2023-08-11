package com.hoaxify.ws.file;

import java.util.Date;

import com.hoaxify.ws.hoax.Hoax;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class FileAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	// bu attachment'in hangi hoax ile iliskili oldugu bilgisi
	// bir hoax bir tane file ile iliskili olabilir
	@OneToOne
	private Hoax hoax;
}
