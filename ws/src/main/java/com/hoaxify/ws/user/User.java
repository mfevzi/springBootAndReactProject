package com.hoaxify.ws.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.hoaxify.ws.auth.Token;
import com.hoaxify.ws.hoax.Hoax;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data // lombok kutuphanesi sayesinde tanimlanan degiskenlere ait getter setter fonksiyonlarini bizim yerimizie tanimlanmis olur
@Entity
@Table(name = "users") // @Table ile databasedeki tablo adını istediğimiz şekilde belirleyebiliriz
//'UserDetails' interface'si spring security'de gerekli oldugundan implement edildi
public class User implements UserDetails {
	private static final long serialVersionUID = 1088472689992904045L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // default hali otomatik olarak id degeri uretir
	private long id;
	
	// kendimiz biz key olusturduk ve buna ozel validation mesaji gosterecegiz
	@NotNull(message = "{hoaxify.constraints.username.NotNull.message}") 
	@Size(min = 4, max = 255)
	@UniqueUsername
	private String kullaniciAdi;

	@NotNull
	@Size(min = 4, max = 15)
	private String displayName;

	@NotNull
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).*$", message = "{hoaxify.constraints.password.Pattern.message}")
	@Size(min = 8, max = 255)
	private String password;
	
	//bu alanin 'large object' (Lob) oldugunu belirtelim. File tipi string'e donusturuldugunde boyutu buyuk oldugundan
	//@Lob
	private String image;
	
	// bir user'in birden fazla hoax'i olabilir. Gerekli iliskiyi kuralim
	// (mappedBy = "user") Hoax tablosundaki 'user' alani bu iliskideki foreign key'dir anlami tasir
	// user'i sildigimizde ona ait olan hoax'larda silinecek (cascade = CascadeType.REMOVE).
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE) 
	private List<Hoax> hoaxesHoaxs;
	
	// bir kullanicinin birden fazla token'i olabilir
	@OneToMany(mappedBy = "user")
	private List<Token> tokens;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("Role_user");
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	// asagidaki kontrolleri bu uygulamada yapmadigimizdan hepsini true yaptik

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
