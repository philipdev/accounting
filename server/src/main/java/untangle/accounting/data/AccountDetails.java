package untangle.accounting.data;

import java.util.Objects;

public record AccountDetails(String account, AccountEntryData[] transactions, Double debit, Double credit) {
	public AccountDetails {
		Objects.requireNonNull(account);
		Objects.requireNonNull(transactions);
	}

}
