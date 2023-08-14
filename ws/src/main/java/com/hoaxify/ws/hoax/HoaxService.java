package com.hoaxify.ws.hoax;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.file.FileAttachment;
import com.hoaxify.ws.file.FileAttachmentRepository;
import com.hoaxify.ws.hoax.vm.HoaxSubmitVM;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;
import com.hoaxify.ws.user.UserService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;

@Service
public class HoaxService {
	
	HoaxRepository hoaxRepository;
	UserService userService;
	FileAttachmentRepository fileAttachmentRepository;

	public HoaxService(HoaxRepository hoaxRepository, UserService userService, 
			FileAttachmentRepository fileAttachmentRepository) {
		this.hoaxRepository = hoaxRepository;
		this.userService = userService;
		this.fileAttachmentRepository = fileAttachmentRepository;
	}

	public void save(HoaxSubmitVM hoaxSubmitVM, User user) {
		Hoax hoax = new Hoax();
		hoax.setContent(hoaxSubmitVM.getContent());
		hoax.setUser(user);
		hoax.setTimestamp(new Date());
		hoaxRepository.save(hoax);
		
		// hoax kaydetme isleminin ardindan eger hoax bir file attachment iceriyorsa..
		// ..o file'i bulup hoax ile iliskili oldugu alani guncelleyelim
		Optional<FileAttachment> optionalFileAttachment = fileAttachmentRepository.findById(hoaxSubmitVM.getAttachmentId());
		// database'de boyle bir dosya varsa (isPresent)
		if(optionalFileAttachment.isPresent()) {
			// fileAttachment objesini al
			FileAttachment fileAttachment = optionalFileAttachment.get();
			// objenin hoax field'ini guncelle/doldur
			fileAttachment.setHoax(hoax);
			fileAttachmentRepository.save(fileAttachment);
		}
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
	public Page<Hoax> getOldHoaxes(Long id, String username, Pageable page) {
		// jpa specification kullanimi
		// verilen id degerinden daha kucuk id'li hoaxlari getir
		Specification<Hoax> specification = idLessThan(id);
		if(username != null) {
			// username bilgisine sahip kullanicinin varligini kontrol edelim
			User inDb = userService.getByUsername(username);
			// eger username bilgisi varsa specification'a user bilgisinin sorgulandigi query'i de dahil edelim
			specification = specification.and(userIs(inDb));
		}
		
		//hoaxRepository'de specification implementasyonu yaptigimiz icin artik specification metotlarina erisebiliriz
		//findAll metodu icine specification ve pageable obje alir. Cevap olarak Page<Hoax> doner
		return hoaxRepository.findAll(specification, page);
	}
	
	//verdigimiz hoax id'den buyuk olan hoax id'lerin sayisini ver
	public long getNewHoaxCount(Long id, String username) {
		Specification<Hoax> specification = idGreaterThan(id);
		if (username != null) {
			User inDb = userService.getByUsername(username);
			specification = specification.and(userIs(inDb));
		}
		return hoaxRepository.count(specification);
	}
	
	//verilen id degerinden daha buyuk olan id'li hoax'larin listesini yeniden eskiye sirali ver
	public List<Hoax> getNewHoaxes(Long id, String username, Sort sort) {
		Specification<Hoax> specification = idGreaterThan(id);
		if (username != null) {
			User inDb = userService.getByUsername(username);
			specification = specification.and(userIs(inDb));
		}
		return hoaxRepository.findAll(specification, sort);
	}
	
	//query olusturmak icin jpa'nin bize sundugu Specification objesini kullanabiliriz.
	//bu sayede birden fazla query'i tek query'de toplama imkani elde ederiz
	//asagidaki metot icine aldigi 'id' degerini 'Hoax' turundeki objenin 'id' alani ile kiyaslar ve daha kucuk id...
	//..degerine sahip hoax'i getirir
	Specification<Hoax> idLessThan(long id) {
		//tek metotlu bir interface'i arrow function ile kullanabiliriz
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.lessThan(root.get("id"), id);
		};
	}
	
	//icindeki user objesi bizim verdigimiz user objesine esit olan hoax'lari getir
	Specification<Hoax> userIs(User user) {
		// tek metotlu bir interface'i arrow function ile kullanabiliriz
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("user"), user);
		};
	}
	
	// hoax id bizim verdigimiz id'den buyuk olanlari getir
	Specification<Hoax> idGreaterThan(long id) {
		//tek metotlu bir interface'i arrow function ile kullanabiliriz
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.greaterThan(root.get("id"), id);
		};
	}

	public void deleteHoax(long id) {
		hoaxRepository.deleteById(id);
	}
	
	
	
}
