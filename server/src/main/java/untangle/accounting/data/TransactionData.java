package untangle.accounting.data;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public record TransactionData(LocalDateTime executedAt, TransactionEntryData[] entries, Optional<String> description) {
	public TransactionData {
		Objects.requireNonNull(executedAt);
		Objects.requireNonNull(entries);
		Objects.requireNonNull(description);
		if(entries.length == 0 || entries.length % 2 != 0) {
			throw new IllegalArgumentException("TransactionData.entries.length may not be zero and must be divisible by 2");
		}
	}

}
