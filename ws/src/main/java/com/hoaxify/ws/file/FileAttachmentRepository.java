package com.hoaxify.ws.file;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaxify.ws.user.User;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
	// date field'i bizim verdigimiz date bilgisinden once olan ve hoax kismi bos olan file listesini getir
	List<FileAttachment> findByDateBeforeAndHoaxIsNull(Date date);
	
	// FileAttachment'in icinde Hoax, Hoax icinde de User var ve bunlarin arasinda iliski var..
	// ..dolayisi ile nested yapida diyoruz ki; verdigimiz user'a ait hoax'lardaki fileAttachment'lari getir
	List<FileAttachment> findByHoaxUser(User user);
}
