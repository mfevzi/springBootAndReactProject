package com.hoaxify.ws.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data //lombok kutuphanesi sayesinde tanimlanan degiskenlere ait getter setter fonksiyonlarini bizim yerimizie tanimlanmis olur
@Entity
@Table(name="users") //@Table ile databasedeki tablo adını istediğimiz şekilde belirleyebiliriz
public class User {
	@Id
	@GeneratedValue //default hali otomatik olarak id degeri uretir
	private long id;
	private String userName;
	private String displayName;
	private String password;
}
