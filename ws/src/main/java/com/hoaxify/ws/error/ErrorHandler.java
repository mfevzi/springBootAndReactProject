package com.hoaxify.ws.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class ErrorHandler implements ErrorController {

	@Autowired
	private ErrorAttributes errorAttributes;

	// spring'in basic error error controller'indan ziyade kendimiz error controller
	// yazmis olduk
	@RequestMapping("/error") // burasi error'lar icin calissin diyoruz
	ApiError handleError(WebRequest webRequest) {
		Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(webRequest,
				ErrorAttributeOptions.of(Include.MESSAGE, Include.BINDING_ERRORS));
		// asagidaki degerlerin 'getErrorAttributes' metodunda yer aldigini biliyoruz.
		// Nerede geldi diye merak etmeyelim
		String message = (String) attributes.get("message");
		String path = (String) attributes.get("path");
		int status = (int) attributes.get("status");
		// apiError nesnemizi gerekli parametreleri alacak sekilde olusturalim
		ApiError apiError = new ApiError(status, message, path);

		// attributes nesnesinin 'errors' ozelligi her zaman dolu olmayabilir. Bu yuzden
		// kontrol yapmak gerekli
		if (attributes.containsKey("errors")) {// bu attributes'lerin icinde errors varsa
			// 'ApiError' sinifimizdaki 'validationErrors' map'ine de hangi error ve bu
			// error'un aciklamasi bilgilerini set edelim
			// bunu 'UserController' sinifimizda yapmistik ancak burada genellestirip orada
			// tekil haldeki metodu iptal edecegiz
			// oncelikle 'attributes' nesnemizin 'errors' ozelliginden gerekli bilgileri
			// alalim
			List<FieldError> fieldErrorsList = (List<FieldError>) attributes.get("errors");
			// apiError nesnemizin 'validationErrors' ozelligini doldurmak icin map
			// olusturalim
			Map<String, String> validationErrors = new HashMap<>();
			for (FieldError fieldError : fieldErrorsList) {
				validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			// validationError'lari da set edelim
			apiError.setValidationErrors(validationErrors);
		}
		return apiError;
	}
}
