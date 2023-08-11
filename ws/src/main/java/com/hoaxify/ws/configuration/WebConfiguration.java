package com.hoaxify.ws.configuration;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Autowired
	AppConfiguration appConfiguration;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// burada diyoruz ki eger soyle bir url gelirse boyle davran..
		// ..yani; "/images" ile baslayan request atarsak git "file/picture-storage"
		// adresinde o dosyayi ara-bul
		// ornegin; http://localhost:8080/images/profile.png request'inde "images/"ten
		// sonraki ismi al ve belirtilen adreste..
		// ..bu dosyayi ara
		registry.addResourceHandler("/images/**")
				.addResourceLocations("file:./" + appConfiguration.getUploadPath() + "/")
				// bu file client tarafinda (browser) bir sene boyunca saklansin
				.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
	}

	// uygulama baslarken image dosyalarinin kaydedilecegi klasoru olusturacak metot
	// yazalim
	@Bean
	CommandLineRunner createStorageDirectories() {
		return (args) -> {
			createFolder(appConfiguration.getUploadPath());
			createFolder(appConfiguration.getProfileStoragePath());
			createFolder(appConfiguration.getAttachmentStoragePath());
		};
	}

	private void createFolder(String path) {
		// path degerini application.yaml'den alacak klasor olusturalim
		File folder = new File(path);
		// boyle bir klasorun zaten var olup olmadigini ve bunun bir dosya degil klasor
		// olup olmadigini kontrol edelim
		boolean folderExist = folder.exists() && folder.isDirectory();
		// eger sartlar uygunsa bu klasoru olusturalim
		if (!folderExist) {
			folder.mkdir();
		}
	}
}
