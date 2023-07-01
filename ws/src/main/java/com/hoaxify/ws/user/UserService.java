package com.hoaxify.ws.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.error.NotFoundException;
import com.hoaxify.ws.user.vm.UserUpdateVM;

@Service
public class UserService {

	// @Autowired
	// dependency injection islemini bu anotasyon haricinde constructor metot ile de
	// yapabiliriz. Ornegi asagida
	UserRepository userRepository;

	// spring security kullanarak password sifreleme yapacagiz
	PasswordEncoder passwordEncoder;

	// @Autowired eger sinif icinde sadece bir tane constructor varsa 'AutoWired'
	// anotasyonuna ihtiyac duyulmaz. Koysak da olur
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public void save(User user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword())); // bu sekilde encode edilmis bir string
																			// tekrar decode edilemez
		userRepository.save(user);
	}

	// verileri sayfa sayfa alabilmek icin spring data'nin pageable nesnesini
	// kullaniyoruz
	public Page<User> getirTumKullanicilar(Pageable page, User user) {
		// eger loggin olmus kullanici varsa o kullanicinin olmadigi kullanici listesini
		// donelim
		if (user != null) {
			return userRepository.findByKullaniciAdiNot(user.getKullaniciAdi(), page);
		}
		// veri tabanindan donen listeyi sayfa sayfa almak istersek donus tipi
		// 'pageable' olan metodu kullaniriz
		return userRepository.findAll(page);
	}

	public User getByUsername(String username) {
		User userInDbUser = userRepository.findByKullaniciAdi(username);
		if (userInDbUser == null) {
			throw new NotFoundException();
		}
		return userInDbUser;
	}

	// kullanicinin displayName bilgisini guncelleyen metot yazalim
	public User updateUser(String username, UserUpdateVM updatedUser) {
		// once username bilgisinden var olan kullanicimizi bulalim
		User userUpdate = getByUsername(username);
		// request'ten gelen ve UserUpdateVM tipine donusturulmus data'yi yeni
		// displayName olarak objeye ata
		userUpdate.setDisplayName(updatedUser.getDisplayName());
		// eger kullanici yeni bir profil fotosu gonderdiyse bunu alalim
		if (updatedUser.getImage() != null) {
			userUpdate.setImage(updatedUser.getImage());
		}
		// save metodu kaydettigi/update ettigi entity'i return eder
		return userRepository.save(userUpdate);
	}

}
