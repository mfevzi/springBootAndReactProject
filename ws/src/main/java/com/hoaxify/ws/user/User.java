package com.hoaxify.ws.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.hoaxify.ws.shared.Views;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data //lombok kutuphanesi sayesinde tanimlanan degiskenlere ait getter setter fonksiyonlarini bizim yerimizie tanimlanmis olur
@Entity
@Table(name="users") //@Table ile databasedeki tablo adını istediğimiz şekilde belirleyebiliriz
public class User {
	@Id
	@GeneratedValue //default hali otomatik olarak id degeri uretir
	private long id;
	
	@NotNull(message = "{hoaxify.constraints.username.NotNull.message}") //kendimiz biz key olusturduk ve buna ozel validation mesaji gosterecegiz
	@Size(min = 4, max = 255)
	@UniqueUsername
	//jsonView kullanarak alan isaretlemesi yaptik@JsonView(Views.Base.class) 
	private String kullaniciAdi;
	
	@NotNull
	@Size(min = 4, max = 15)
	@JsonView(Views.Base.class) //jsonView kullanarak alan isaretlemesi yaptik
	private String displayName;
	
	@NotNull
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).*$", 
	message = "{hoaxify.constraints.password.Pattern.message}")
	@Size(min = 8, max = 255)
	private String password;
	
	@JsonView(Views.Base.class) //jsonView kullanarak alan isaretlemesi yaptik
	private String image;
}
