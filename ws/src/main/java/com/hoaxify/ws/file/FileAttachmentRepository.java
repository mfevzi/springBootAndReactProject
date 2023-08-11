package com.hoaxify.ws.file;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
	// date field'i bizim verdigimiz date bilgisinden once olan ve hoax kismi bos olan file listesini getir
	List<FileAttachment> findByDateBeforeAndHoaxIsNull(Date date);
}
