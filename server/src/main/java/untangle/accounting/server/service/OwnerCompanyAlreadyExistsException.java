package untangle.accounting.server.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.CONFLICT, reason="OWNER_EXISTS")
public class OwnerCompanyAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

}
