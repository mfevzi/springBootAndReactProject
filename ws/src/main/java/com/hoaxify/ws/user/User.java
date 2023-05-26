package com.hoaxify.ws.user;

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
	@NotNull
	@Size(min = 4, max = 255)
	private String kullaniciAdi;
	@NotNull
	@Size(min = 4, max = 15)
	private String displayName;
	@NotNull
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).*$", message = "şifreniz en az 1 büyük harf, 1 küçük harf ve 1 rakam içermelidir")
	@Size(min = 8, max = 255)
	private String password;
}
