package com.hoaxify.ws.error;

import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
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
