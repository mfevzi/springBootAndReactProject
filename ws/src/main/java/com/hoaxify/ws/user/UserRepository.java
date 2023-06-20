package com.hoaxify.ws.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	// kullaniciAdi parametresine gore arama yapip sonuc dondurecek bir metot
	// tanimliyoruz
	User findByKullaniciAdi(String kullaniciAdi); // spring bizim yerimize arka planda gerekli veri tabani sorgulamasini
													// yapacak. Bu kadar kod yazmak yeterli. Valla super

	// kullaniciAdi bu olmayanlari getir
	Page<User> findByKullaniciAdiNot(String kullaniciAdi, Pageable page);
}
