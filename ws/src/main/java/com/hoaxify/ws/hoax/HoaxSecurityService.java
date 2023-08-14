package com.hoaxify.ws.hoax;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.user.User;

/**
 * bir hoax'in silinebilirligini kontrol eden securtiy class olusturalim
 * 
 * @author mehmetfevziakdeniz
 *
 */
@Service
public class HoaxSecurityService {
	
	// spring'in bu class'tan bir obje olusturmasini saglayalim
	@Autowired
	HoaxRepository hoaxRepository;
	
	public boolean isAllowedToDelete(long id, User loggedInUser) {
		// simdilik asagidaki kodu iptal edelim ve current user'a gore islem yapilmasin
		/*
		// id degerinden yola cikarak hoax'i bulalim
		Optional<Hoax> optionalHoax = hoaxRepository.findById(id);
		// eger boyle bir hoax yoksa
		if (!optionalHoax.isPresent()) {
			return false;
		}
		// optional hoax'tan hoax'imizi alalim
		Hoax hoax = optionalHoax.get();
		// eger hoax'in sahibi su anda login olan kullanici degilse
		if (hoax.getUser().getId() != loggedInUser.getId()) {
			return false;
		}
		return true;
		*/
		return true;
	}
}
