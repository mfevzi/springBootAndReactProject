package com.hoaxify.ws.hoax;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;
import com.hoaxify.ws.user.UserService;

import jakarta.validation.Valid;

@Service
public class HoaxService {
	
	HoaxRepository hoaxRepository;
	UserService userService;

	public HoaxService(HoaxRepository hoaxRepository, UserService userService) {
		this.hoaxRepository = hoaxRepository;
		this.userService = userService;
	}

	public void save(Hoax hoax, User user) {
		hoax.setUser(user);
		hoax.setTimestamp(new Date());
		hoaxRepository.save(hoax);
	}

	public Page<Hoax> getHoaxes(Pageable page) {
		return hoaxRepository.findAll(page);
	}

	public Page<Hoax> getHoaxesOfUser(String username, Pageable page) {
		//username bilgisine sahip kullanicinin varligini kontrol edelim
		User inDb = userService.getByUsername(username);
		return hoaxRepository.findByUser(inDb, page);
	}
	
	//sirali sekilde bulunan hoax listesinden id numarasi verilen bir hoax'tan onceki hoax'lari getiren metot
	public Page<Hoax> getOldHoaxes(Long id, Pageable page) {
		return hoaxRepository.findByIdLessThan(id, page);
	}

	public Page<Hoax> getOldHoaxesOfUser(Long id, String username, Pageable page) {
		// username bilgisine sahip kullanicinin varligini kontrol edelim
		User inDb = userService.getByUsername(username);
		return hoaxRepository.findByIdLessThanAndUser(id, inDb, page);
	}
	
	//verdigimiz hoax id'den buyuk olan hoax id'lerin sayisini ver
	public long getNewHoaxCount(Long id) {
		return hoaxRepository.countByIdGreaterThan(id);
	}
	
	// verdigimiz hoax id'den buyuk olan ve kullaniciya ait olan hoaxlarin sayisini ver
	public long getNewHoaxOfUserCount(Long id, String username) {
		User inDb = userService.getByUsername(username);
		return hoaxRepository.countByIdGreaterThanAndUser(id, inDb);
	}
	
	//verilen id degerinden daha buyuk olan id'li hoax'larin listesini yeniden eskiye sirali ver
	public List<Hoax> getNewHoaxes(Long id, Sort sort) {
		return hoaxRepository.findByIdGreaterThan(id, sort);
	}

	public List<Hoax>  getNewHoaxesOfUser(Long id, String username, Sort sort) {
		User inDb = userService.getByUsername(username);
		return hoaxRepository.findByIdGreaterThanAndUser(id, inDb, sort);
	}
	
	
}
