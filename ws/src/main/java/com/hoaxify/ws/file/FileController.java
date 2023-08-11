package com.hoaxify.ws.file;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {
	
	@Autowired
	FileService fileService;
	// file upload islemi icin post mapping kullaniyoruz
	// bu metot da arayuzden kullanicinin file upload ettigi farz ediyoruz
	@PostMapping("/api/hoax-attachments")
	// multipart olarak verilen file'dan degeri 'image' olan parametreyi al
	FileAttachment saveHoaxAttachment(@RequestParam(name = "image") MultipartFile multipartFile) {
		return fileService.saveHoaxAttachment(multipartFile);
	}
}
