package untangle.accounting.server.rest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import untangle.accounting.server.service.OwnerCompanyAlreadyExistsException;

@RestControllerAdvice
public class ControllerAdvice {
	
	@ExceptionHandler(OwnerCompanyAlreadyExistsException.class)
	public String ownerAlreadyExist() {
		return "TEST";
	}
}
