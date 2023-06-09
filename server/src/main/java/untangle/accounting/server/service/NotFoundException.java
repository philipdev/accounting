package untangle.accounting.server.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NotFoundException(String msg,Throwable cause) {
		super(msg,cause);
	}
	
	public NotFoundException(String msg) {
		super(msg);
	}
}
