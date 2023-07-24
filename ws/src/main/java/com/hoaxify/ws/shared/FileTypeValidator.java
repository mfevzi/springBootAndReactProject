package com.hoaxify.ws.shared;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.hoaxify.ws.file.FileService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//anotasyonumuzun adi 'ProfileImage' ve validate edecegimiz field'in tipi de String olacak
public class FileTypeValidator implements ConstraintValidator<FileType, String> {

	@Autowired
	FileService fileService;

	// FileType anotasyonundaki 'types' arrayine erisirken kullanacagiz
	String[] types;

	// FileType anotasyonundaki 'types' arrayine erisim icin 'initialize' metodunu override edelim
	@Override
	public void initialize(FileType constraintAnnotation) {
		this.types = constraintAnnotation.types();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// eger kullanici bir image yuklememis ise true deger dondurelim
		if (value == null || value.isEmpty()) {
			return true;
		}
		String fileType = fileService.detectType(value);
		for (String supportedTypes : this.types) {
			// eger dosya turu bizim izin verdigimiz turden ise true donsun
			if (fileType.contains(supportedTypes)) {
				return true;
			}
		}
		return false;
	}
}
