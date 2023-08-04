package com.hoaxify.ws.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true) //@PreAuthorize kullanimi icin ekledik (spring security)
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic().authenticationEntryPoint(new AuthEntryPoint());
		
		//spring security'den dolayi h2 vt'ye baglanma problemini onlemek icin
		http.headers().frameOptions().disable();
		
		//'/api/auth'ya gelen requestler authenticated olmali yani icinde gerekli parametreleri barindirmali
		http
		.authorizeHttpRequests()
		.requestMatchers(HttpMethod.POST, "/api/auth").authenticated()
		//bu request'i gondermeye yetkili mi kullanici diye sorguluyoruz
		.requestMatchers(HttpMethod.PUT, "/api/users/{username}").authenticated()
		.and()
		.authorizeHttpRequests().anyRequest().permitAll(); //yukaridaki requestlerin haricindekilere izin veriyoruz. Herhangi bir kontrole sokmuyoruz
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //security ile ilgili session uretme diyoruz
		return http.build();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
