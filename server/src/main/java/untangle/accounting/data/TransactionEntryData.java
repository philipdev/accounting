package untangle.accounting.data;

import java.util.Objects;

public record TransactionEntryData(String account, Double debit, Double credit) {
	public TransactionEntryData {
		Objects.requireNonNull(account);
		Objects.requireNonNull(debit);
		Objects.requireNonNull(credit);
		
		if(debit == 0d && credit == 0d) {
			throw new IllegalArgumentException("Both debit and credit are zero");
		} else if(debit !=0d && credit !=0d) {
			throw new IllegalArgumentException("Either debit or credit needs to be zero");
		}
		
	}
}
