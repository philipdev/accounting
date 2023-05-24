package untangle.accounting.server.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedToGeneratedReceipt extends Exception {

	private static final long serialVersionUID = 1L;

	public FailedToGeneratedReceipt(Throwable cause) {
		super(cause);
	}

}
