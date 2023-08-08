package com.hoaxify.ws.hoax;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hoaxify.ws.user.User;
//specification kullanimi icin 'JpaSpecificationExecutor' class'ini extend etmeliyiz
public interface HoaxRepository extends JpaRepository<Hoax, Long>, JpaSpecificationExecutor<Hoax>{
	Page<Hoax> findByUser(User user, Pageable page);
	
	//*** asagidaki metotlarin hepsini hoax service kisminda specification ile yaptik ***
	
	//metot icine gonderilen id degerinden onceki hoaxlari getirelim
	//Page<Hoax> findByIdLessThan(Long id, Pageable page);
	
	//verilen hoax id'den daha kucuk olanlari ve user'a ait olanlari getir
	//Page<Hoax> findByIdLessThanAndUser(long id, User user, Pageable page);
	
	//count islemleri icin de spring'in bize sundugu name convention'lar var
	//bizim verdigimiz hoax id'den buyuk olan hoaxlarin sayisini ver
	//long countByIdGreaterThan(long id);
	
	//verilen hoax id'den daha buyuk olan ve user'a ait olan hoaxlarin sayisini getir
	//long countByIdGreaterThanAndUser(long id, User user);
	
	//verdgimiz hoax id'den daha yeni hoaxlarin listesini yeniden eskiye sirali ver
	//List<Hoax> findByIdGreaterThan(long id, Sort sort);
	
	//verdgimiz hoax id'den daha yeni ve kullaniciya ait hoaxlarin listesini yeniden eskiye sirali ver
	//List<Hoax> findByIdGreaterThanAndUser(long id, User user, Sort sort);
}
