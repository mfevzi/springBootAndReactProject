package com.hoaxify.ws.hoax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.hoax.vm.HoaxVM;
import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.user.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class HoaxController {
	
	@Autowired
	HoaxService hoaxService;
	
	@PostMapping("/hoaxes")
	//@valid anotasyonu, hoax nesnesinin entity olarak tanimlandigi siniftaki kurallara uygunlugunu kontrol eder
	GenericResponse saveHoax(@Valid @RequestBody Hoax hoax, @CurrentUser User user) {
		hoaxService.save(hoax, user);
		return new GenericResponse("Hoax is saved");
	}
	
	@GetMapping("/hoaxes")
	//@PageableDefault anotasyonu ile paging olayinda konfigurasyon yapilabilir..
	//..hoax nesnesinin 'id' ozelligine gore sort islemi yap ve buyukten kucuge dogru git
	Page<HoaxVM> getHoaxes(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable page) {
		return hoaxService.getHoaxes(page).map(HoaxVM::new);
	}
}
