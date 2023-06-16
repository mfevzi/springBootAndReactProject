package com.hoaxify.ws.user.vm;

import com.hoaxify.ws.user.User;

/**
 * bu sinif page olarak aldigimiz verileri json'a cevirirken tum alanlarin degil de istedigimiz alanlarin json'a cevrilmesi..
 * ..icin kullanilacak.
 */

import lombok.Data;

@Data
public class UserVM {
	private String kullaniciAdi;
	private String displayName;
	private String image;
	
	public UserVM(User user) {
		this.kullaniciAdi = user.getKullaniciAdi();
		this.displayName = user.getDisplayName();
		this.image = user.getImage();
	}
	
	
}
