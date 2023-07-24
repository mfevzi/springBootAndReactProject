package com.hoaxify.ws.user.vm;


import com.hoaxify.ws.shared.FileType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Bu class user nesnesinin sadece displayName alaninin guncellenmesi isleminde request'ten gelen json veriyi..
 * ..objeye donusturme asamasinda (@RequestBody) kullanilir.
 */

import lombok.Data;

@Data
public class UserUpdateVM {
	@NotNull
	@Size(min = 4, max = 15)
	private String displayName;
	
	//hangi turde iamge'lerin gecerli oldugunu tanimlayalim
	@FileType(types = { "jpeg", "png" })
	private String image;
}
