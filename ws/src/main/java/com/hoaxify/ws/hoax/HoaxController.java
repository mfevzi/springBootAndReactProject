package com.hoaxify.ws.hoax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.hoax.vm.HoaxSubmitVM;
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
	GenericResponse saveHoax(@Valid @RequestBody HoaxSubmitVM hoax, @CurrentUser User user) {
		hoaxService.save(hoax, user);
		return new GenericResponse("Hoax is saved");
	}
	
	@GetMapping("/hoaxes")
	//@PageableDefault anotasyonu ile paging olayinda konfigurasyon yapilabilir..
	//..hoax nesnesinin 'id' ozelligine gore sort islemi yap ve buyukten kucuge dogru git
	Page<HoaxVM> getHoaxes(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable page) {
		return hoaxService.getHoaxes(page).map(HoaxVM::new);
	}
	
	//belli bir hoax id'ye bagli olarak getirecegimiz hoaxlar icin metot
	//id degeri long tipinde oldugundan sadece rakam icermesi gerektigini belirtelim..
	//..gelen request'te 0'dan 9'a kadar rakam olabilir ve birden fazla rakam icerebilir (+)
	//@requestParam ile opsiyonel olarak request parametresi alalim. Opsiyonel oldugu 'required' ifadesinden gelir
	//eger elimizdeki hoax'tan sonra yeni hoax'lar gelmis ise count degeri true olsun
	//donus tipi degisken oldugundan 'ResponseEntity' kullandik
	//birden fazla path tanimlamak mumkun. Bunun icin suslu parantez ve virgul kullaniyoruz
	@GetMapping({"/hoaxes/{id:[0-9]+}", "users/{username}/hoaxes/{id:[0-9]+}"})
	ResponseEntity<?> getHoaxesRelative(@PathVariable Long id, 
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable page,
			@PathVariable(required = false) String username,
			@RequestParam(name = "count", required = false, defaultValue = "false") boolean count,
			@RequestParam(name = "direction", defaultValue = "before") String direction) {
		//eger count degeri varsa
		if(count) {
			long newHoaxCount = hoaxService.getNewHoaxCount(id, username);
			//count key'ine sahip bir json olusturalim
			Map<String, Long> response = new HashMap<>();
			response.put("count", newHoaxCount);
			//donus tipimiz degisken oldugundan
			return ResponseEntity.ok(response);
		}
		//direction parametresi 'after' ise
		if (direction.equals("after")) {
			// listenin yeniden eskiye gore siralanmasini istedigimizde 'page.getSort()' verdik
			//hoax turunde donen cevabi hoaxVM turunde listeye donusturmek icin java 8 stream kullandik
			List<HoaxVM> newHoaxs = hoaxService.getNewHoaxes(id, username, page.getSort())
					.stream().map(HoaxVM::new).collect(Collectors.toList());
			return ResponseEntity.ok(newHoaxs);
		}
		//donus tipimiz degisken oldugundan
		return ResponseEntity.ok(hoaxService.getOldHoaxes(id, username, page).map(HoaxVM::new));
	}
	
	@GetMapping("users/{username}/hoaxes")
	Page<HoaxVM> getUserHoaxes(@PathVariable String username, @PageableDefault(sort = "id", direction = Direction.DESC) Pageable page) {
		return hoaxService.getHoaxesOfUser(username, page).map(HoaxVM::new);
	}
	
	//burada opsiyonel olarak request param ile 'count' kullaniyoruz.
	//count true ise verilen hoax id'ye gore kullaniciya ait yeni hoaxlarin sayisini dondurecegiz
	//count false ise verilen hoax id'den daha eski olan kullanici hoaxlarini getirecegiz
	//donus tipi degisken oldugundan 'response entity' kullaniyoruz
	/*
	 * @GetMapping("users/{username}/hoaxes/{id:[0-9]+}") ResponseEntity<?>
	 * getUserHoaxesRelative(@PathVariable Long id,
	 * 
	 * @PathVariable String username, @PageableDefault(sort = "id", direction =
	 * Direction.DESC) Pageable page,
	 * 
	 * @RequestParam(name = "count", required = false, defaultValue = "false")
	 * boolean count,
	 * 
	 * @RequestParam(name = "direction", defaultValue = "before") String direction)
	 * { // eger count degeri varsa if (count) { long newHoaxCount =
	 * hoaxService.getNewHoaxOfUserCount(id, username); // count key'ine sahip bir
	 * json olusturalim Map<String, Long> response = new HashMap<>();
	 * response.put("count", newHoaxCount); // donus tipimiz degisken oldugundan
	 * return ResponseEntity.ok(response); } // direction parametresi 'after' ise if
	 * (direction.equals("after")) { // listenin yeniden eskiye gore siralanmasini
	 * istedigimizde 'page.getSort()' verdik // hoax turunde donen cevabi hoaxVM
	 * turunde listeye donusturmek icin java 8 stream kullandik List<HoaxVM>
	 * newHoaxs = hoaxService.getNewHoaxesOfUser(id, username,
	 * page.getSort()).stream().map(HoaxVM::new) .collect(Collectors.toList());
	 * return ResponseEntity.ok(newHoaxs); } return
	 * ResponseEntity.ok(hoaxService.getOldHoaxesOfUser(id, username,
	 * page).map(HoaxVM::new)); }
	 */
	
	@DeleteMapping("/hoaxes/{id:[0-9]+}")
	// hoax'in silinebilirligini kontrol eden servis sinifimizi cagirip kontrolden gecirelim
	// 'HoaxSecurityService' class'inin basina '@' koyarak ve camelCase olarak yazarak..
	// .. spring'e diyoruz ki senin boyle bir bean'in var. Bu bean'nin 'isAllowedToDelete' isimli..
	// .. isimli metodunu calistirir. Metot parametrelerini ise '#' yardimi ile metoda gonderiyoruz
	@PreAuthorize("@hoaxSecurityService.isAllowedToDelete(#id, #loggedInUser)")
	GenericResponse deleteHoax(@PathVariable long id, @CurrentUser User loggedInUser) {
		hoaxService.deleteHoax(id);
		return new GenericResponse("Hoax removed");
	}
}
