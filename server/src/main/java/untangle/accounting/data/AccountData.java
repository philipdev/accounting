package untangle.accounting.data;

import java.util.Objects;

public record AccountData(String accountName){
	public AccountData {
		Objects.requireNonNull(accountName);
	}
}
