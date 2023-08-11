package com.hoaxify.ws.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hoaxify.ws.configuration.AppConfiguration;

@Service
@EnableScheduling //tarih ayarli olarak metot calistirmak istedigimizde kullanabilirz
public class FileService {

	AppConfiguration appConfiguration;
	Tika tika;
	FileAttachmentRepository fileAttachmentRepository;

	public FileService(AppConfiguration appConfiguration, FileAttachmentRepository fileAttachmentRepository) {
		this.appConfiguration = appConfiguration;
		this.tika = new Tika();
		this.fileAttachmentRepository = fileAttachmentRepository;
	}

	// base64 string degerini file tipine donusturen fonksiyon
	public String writeBase64EncodedStringToFile(String image) throws IOException {
		String fileName = generateRandomName();
		File target = new File(appConfiguration.getUploadPath() + "/" + fileName);
		OutputStream outputStream = new FileOutputStream(target);
		byte[] base64encoded = Base64.getDecoder().decode(image);
		outputStream.write(base64encoded);
		outputStream.close();
		return fileName;
	}

	// image'leri dosyaya kaydederken farkli isimlendirmeler yapabilelim diye random
	// string ureten metot yazalim
	public String generateRandomName() {
		// Universal Unique Identifier (UUID)
		// metot '-' ile birbirinden ayrilmis karakterler uretir. Biz bunlari yan yana
		// yazilmis olarak uretelim
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public void deleteFile(String oldImageName) {
		if (oldImageName == null) {
			return;
		}
		// Files class'inin silme metodunu kullanalim
		try {
			Files.deleteIfExists(Paths.get(appConfiguration.getUploadPath(), oldImageName));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String detectType(String value) {
		//base64 encode seklindeki string veriyi (image file) decode edelim
		byte[] base64encoded = Base64.getDecoder().decode(value);
		//byte tipindeki verinin dosya tipini bulalim. Burada 'Tika' sinifininin hazir metodunu kullaniyoruz
		String fileType = tika.detect(base64encoded);
		return fileType;
	}

	public FileAttachment saveHoaxAttachment(MultipartFile multipartFile) {
		String fileName = generateRandomName();
		File target = new File(appConfiguration.getUploadPath() + "/" + fileName);
		try {
			OutputStream outputStream = new FileOutputStream(target);
			outputStream.write(multipartFile.getBytes());
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileAttachment attachment = new FileAttachment();
		attachment.setName(fileName);
		attachment.setDate(new Date());
		return fileAttachmentRepository.save(attachment);
		
	}
	
	// fixedRate anlami; metot hangi periyotta calissin. Milisaniye cinsinden 24
	// saatte bir calissin dedik
	// uygulama run edildigi andan itibaren artık 24 saatte bir calisir
	@Scheduled(fixedRate = 24 * 60 * 60 * 1000)
	public void cleanUpStorage() {
		// date objemiz 24 saat oncesi olan bir tarih olacak. Guncel saatten 24 saatlik
		// mili saniye degerini cikariyoruz
		Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
		List<FileAttachment> filesToBeDeleted = fileAttachmentRepository
				.findByDateBeforeAndHoaxIsNull(twentyFourHoursAgo);
		// silmek istedigimiz file listesini yukarida elde ettik. Simdi silme islemini
		// yapalim
		for (FileAttachment file : filesToBeDeleted) {
			// delete file from directory
			deleteFile(file.getName());
			// delete from database
			fileAttachmentRepository.deleteById(file.getId());
		}
	}

	// scheduled metot test
	@Scheduled(fixedRate = 5 * 1000)
	public void test() {
		System.out.println("Askin olayim Icardi");
	}
}
