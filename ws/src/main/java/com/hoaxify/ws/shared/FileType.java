package com.hoaxify.ws.shared;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.hoaxify.ws.user.UniqueUsernameValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ FIELD}) //bu anotasyonu nerede kullanacagiz
@Retention(RUNTIME)
@Constraint(validatedBy = { FileTypeValidator.class }) //bu anotasyon kullanildigindaki ilgili muhattap sinif
public @interface FileType {
	//bean validation isleminde asagidaki uclunun olmasi sarttir
		String message() default "{hoaxify.constraints.FileType.message}";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };
		
		//file type'lerimizin yazilacagi alani ekleyelim
		String[] types();
}
