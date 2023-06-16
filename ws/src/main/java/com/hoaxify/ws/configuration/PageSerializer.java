package com.hoaxify.ws.configuration;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 
 * @author mehmetfevziakdeniz
 * page objelerini json'a cevirmek icin kullaniyoruz
 *
 */
@JsonComponent //springin bu sinifi taniyip kullanabilmesi icin bu anotasyonu ekliyoruz
public class PageSerializer extends JsonSerializer<Page<?>>{//type kismina soru isareti koyuldugunda tum tipleri kapsar anlami tasir

	@Override
	public void serialize(Page<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		//page objemizi json'a donustururken hangi field'lari alacagimizi biz belirliyoruz
		gen.writeStartObject();
		gen.writeFieldName("content");//page objemiz icine 'content' isimli field aliyordu
		serializers.defaultSerializeValue(value.getContent(), gen);//content field'ini aldik
		//json objemizde olmasini istedigimiz diger field'lari ekleyelim
		gen.writeObjectField("pageable", value.getPageable());
		gen.writeBooleanField("last", value.isLast());
		gen.writeNumberField("totalPages", value.getTotalPages());
		gen.writeNumberField("totalElements", value.getTotalElements());
		gen.writeNumberField("size", value.getSize());
		gen.writeNumberField("number", value.getNumber());
		gen.writeObjectField("sort", value.getSort());
		gen.writeNumberField("numberOfElements", value.getNumberOfElements());
		gen.writeBooleanField("first", value.isFirst());
		gen.writeBooleanField("empty", value.isEmpty());
		//json yazma isimiz bitirelim
		gen.writeEndObject();
	
	} 

}
