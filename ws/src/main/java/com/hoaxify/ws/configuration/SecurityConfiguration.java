package com.hoaxify.ws.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true) //@PreAuthorize kullanimi icin ekledik (spring security)
public class SecurityConfiguration{
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.exceptionHandling().authenticationEntryPoint(new AuthEntryPoint());
		
		//spring security'den dolayi h2 vt'ye baglanma problemini onlemek icin
		http.headers().frameOptions().disable();
		
		//'/api/auth'ya gelen requestler authenticated olmali yani icinde gerekli parametreleri barindirmali
		http
		.authorizeHttpRequests()
		//bu request'i gondermeye yetkili mi kullanici diye sorguluyoruz
		.requestMatchers(HttpMethod.PUT, "/api/users/{username}").authenticated()
		//kullanici login olmadan hoax post edemesin
		.requestMatchers(HttpMethod.POST, "/api/hoaxes").authenticated()
		//kullanici login olmadan file upload edemesin
		.requestMatchers(HttpMethod.POST, "/api/hoax-attachments").authenticated()
		.and()
		.authorizeHttpRequests().anyRequest().permitAll(); //yukaridaki requestlerin haricindekilere izin veriyoruz. Herhangi bir kontrole sokmuyoruz
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //security ile ilgili session uretme diyoruz
		
		// token filter'in once calismasini istiyoruz
		// artik bu eklemeden sonra bizim filtremiz calismaya baslayacak
		http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    // yukarida da yaptigimiz gibi springSecurity ile "TokenFilter" kullanabilmek icin..
    // ..filtreden bir instance olusturuyoruz. Olusturdugumuz bu filtreyi de spring'in security'sinde..
    // ..uygun bir yere pozisyonlayacagiz. Bunun yukaridaki 'filterChain' altina ekleme yaptik.
    @Bean
    TokenFilter tokenFilter() {
    	return new TokenFilter();
    }
}
