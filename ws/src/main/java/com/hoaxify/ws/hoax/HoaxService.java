package com.hoaxify.ws.hoax;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.user.User;

import jakarta.validation.Valid;

@Service
public class HoaxService {
	
	HoaxRepository hoaxRepository;

	public HoaxService(HoaxRepository hoaxRepository) {
		this.hoaxRepository = hoaxRepository;
	}

	public void save(Hoax hoax, User user) {
		hoax.setUser(user);
		hoax.setTimestamp(new Date());
		hoaxRepository.save(hoax);
	}

	public Page<Hoax> getHoaxes(Pageable page) {
		return hoaxRepository.findAll(page);
	}
	
	
}
