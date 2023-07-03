package com.hoaxify.ws.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
//uygulamada kendi property'lerimizi olusturmak ve bu property'leri application.yaml'de kullanirken autoComplete ozelliginden faydalanmak icin
@ConfigurationProperties (prefix = "hoaxify") //application.yaml'da cagrirken oncesinde 'hoaxify' ifadesi yer alsin diyoruz
public class AppConfiguration {
	//asagida camelCase olarak 'uploadPath' seklinde yazilan degisken application.yaml dosyasindaki 'upload-path' ifadeye karsilik gelir
	private String uploadPath;
}
