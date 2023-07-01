package com.hoaxify.ws.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class FileService {
	// base64 string degerini file tipine donusturen fonksiyon
	public String writeBase64EncodedStringToFile(String image) throws IOException {
		String fileName = generateRandomName();
		File target = new File("picture-storage/" + fileName);
		OutputStream outputStream = new FileOutputStream(target);
		byte[] base64encoded = Base64.getDecoder().decode(image);
		outputStream.write(base64encoded);
		outputStream.close();
		return fileName;
	}
	
	//image'leri dosyaya kaydederken farkli isimlendirmeler yapabilelim diye random string ureten metot yazalim
	public String generateRandomName() {
		//Universal Unique Identifier (UUID)
		//metot '-' ile birbirinden ayrilmis karakterler uretir. Biz bunlari yan yana yazilmis olarak uretelim
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
