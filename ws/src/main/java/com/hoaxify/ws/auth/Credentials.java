package com.hoaxify.ws.auth;

import lombok.Data;

/**
 * Bu sinif authentication islemi icin kullaniliyor
 * @author mehmetfevziakdeniz
 *
 */
@Data
public class Credentials {
	
	private String username;
	
	private String password;
}
