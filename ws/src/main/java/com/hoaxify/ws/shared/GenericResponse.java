package com.hoaxify.ws.shared;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor //default constructor icine parametre almaz. Biz bu sekilde icine parametre alan constructor olusturuyoruz
public class GenericResponse {
	private String message;
}
