package untangle.accounting.server.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND, reason="OWNER_DOES_NOT_EXIST")
public class OwnerCompanyDoesNotExistException extends RuntimeException{
	private static final long serialVersionUID = 1L;

}
