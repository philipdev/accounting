package untangle.accounting.data;

import java.util.Objects;
import java.util.Optional;

public record AccountData(Optional<Long> id, AccountType accountType, String accountName, String accountNumber){
	public AccountData {
		Objects.requireNonNull(id);
		Objects.requireNonNull(accountType);
		Objects.requireNonNull(accountName);
		Objects.requireNonNull(accountNumber);
		if(accountNumber.isBlank()) {
			throw new IllegalArgumentException("Account number can not be blank");
		}
	}
}
