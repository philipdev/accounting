package untangle.accounting.data;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public record AccountEntryData(String account, Optional<LocalDateTime> executedAt, Double debit, Double credit, Optional<String> description, Optional<LocalDateTime> createdAt) {
	public AccountEntryData {
		Objects.requireNonNull(account);
		Objects.requireNonNull(executedAt);
		Objects.requireNonNull(debit);
		Objects.requireNonNull(credit);
		Objects.requireNonNull(description);
	}
}
