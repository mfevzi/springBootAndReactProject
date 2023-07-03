package com.hoaxify.ws.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//burada diyoruz ki eger soyle bir url gelirse boyle davran..
		//..yani; "/images" ile baslayan request atarsak git "file/picture-storage" adresinde o dosyayi ara-bul
		//ornegin; http://localhost:8080/images/profile.png request'inde "images/"ten sonraki ismi al ve belirtilen adreste..
		//..bu dosyayi ara
		registry.addResourceHandler("/images/**")
		.addResourceLocations("file:./picture-storage/")
		//bu file client tarafinda (browser) bir sene boyunca saklansin
		.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
	}
}
