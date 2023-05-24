package untangle.accounting.server.entity;

import java.util.Currency;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class Amount {
	private double amount;
	private Currency currency;

	Amount() {

	}

	public Amount(double amount, Currency currency) {
		this.amount = amount;
		this.currency = currency;
	}

	public Amount(double amount, String currency) {
		this(amount, Currency.getInstance(currency));
	}

	public double getAmount() {
		return amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, currency);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Amount other) {
			return Objects.equals(this.amount, other.amount) && Objects.equals(this.currency, other.currency);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Amount[%s %s]".formatted(this.amount, currency.getCurrencyCode());
	}
}
