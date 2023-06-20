package com.hoaxify.ws.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//bu class'in yaratacagi respose cevabinda hhtp status kodu olarak 'not found' olacak
//bir islemde notFoundException firlatirsak bu exception'un respons status kodu 'HttpStatus.NOT_FOUND' (404) olsun dedik
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
	
}
