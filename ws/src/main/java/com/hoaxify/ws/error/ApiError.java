package com.hoaxify.ws.error;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //jackson kutuphanesini kullandik ve dedik ki ..
//..response'larda null olan degerleri dondurme
public class ApiError {
	private int status; //hata kodu
	
	private String message; //hata ile ilgili mesaj
	
	private String path; //hatanin hangi requestte oldugu
	
	private long timestamp = new Date().getTime(); //hatanin olustugu zaman milisaniye cinsinden
	
	private Map<String, String> validationErrors; //hatanin olusmasi ile ilgili bilgi
	public ApiError (int status, String message, String path) {
		this.status = status;
		this.message = message;
		this.path = path;
	}
}
