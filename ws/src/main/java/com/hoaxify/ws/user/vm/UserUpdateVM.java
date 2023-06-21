package com.hoaxify.ws.user.vm;


/**
 * Bu class user nesnesinin sadece displayName alaninin guncellenmesi isleminde request'ten gelen json veriyi..
 * ..objeye donusturme asamasinda (@RequestBody) kullanilir.
 */

import lombok.Data;

@Data
public class UserUpdateVM {
	private String displayName;
}
