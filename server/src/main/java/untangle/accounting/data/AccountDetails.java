package untangle.accounting.data;

import java.util.Objects;

public record AccountDetails(AccountData account, AccountEntryData[] transactions, Double debit, Double credit) {
	public AccountDetails {
		Objects.requireNonNull(account);
		Objects.requireNonNull(transactions);
	}

}
