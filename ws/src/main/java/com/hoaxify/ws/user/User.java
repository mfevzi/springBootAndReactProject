package com.hoaxify.ws.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data // lombok kutuphanesi sayesinde tanimlanan degiskenlere ait getter setter
		// fonksiyonlarini bizim yerimizie tanimlanmis olur
@Entity
@Table(name = "users") // @Table ile databasedeki tablo adını istediğimiz şekilde belirleyebiliriz
//'UserDetails' interface'si spring security'de gerekli oldugundan implement edildi
public class User implements UserDetails {
	private static final long serialVersionUID = 1088472689992904045L;


	@Id
	@GeneratedValue // default hali otomatik olarak id degeri uretir
	private long id;

	@NotNull(message = "{hoaxify.constraints.username.NotNull.message}") // kendimiz biz key olusturduk ve buna ozel
																			// validation mesaji gosterecegiz
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

	private String image;

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
