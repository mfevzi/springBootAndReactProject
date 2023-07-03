package com.hoaxify.ws.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.configuration.AppConfiguration;

@Service
public class FileService {

	@Autowired
	AppConfiguration appConfiguration;

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
}
