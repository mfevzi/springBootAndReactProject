package com.hoaxify.ws.user;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull.List;

@Target({ FIELD}) //bu anotasyonu nerede kullanacagiz
@Retention(RUNTIME)
@Constraint(validatedBy = { UniqueUsernameValidator.class }) //bu anotasyon kullanildigindaki ilgili muhattap sinif
public @interface UniqueUsername {
	//bean validation isleminde asagidaki uclunun olmasi sarttir
	String message() default "Girilen kullanici adi daha once kullanilmis. Lutfen farkli bir kullanici adi giriniz";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
