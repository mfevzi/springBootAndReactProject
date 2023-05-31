package com.hoaxify.ws.shared;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Target({ ElementType.PARAMETER}) //bu anotasyonu metot parametresi kullanacagimiz icin
@Retention(RUNTIME)
@AuthenticationPrincipal //spring security'nin sundugu bir anotasyon. Casting islemi icin kullanilir
public @interface CurrentUser {

}
