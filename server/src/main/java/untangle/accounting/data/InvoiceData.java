package untangle.accounting.data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import untangle.accounting.server.entity.InvoiceState;



public record InvoiceData(Optional<Long> id, CompanyData invoiceTo, Optional<CompanyData> invoiceFrom, LocalDateTime invoiceDate, int daysDue, InvoiceItem[] items, String reference, String description, Optional<InvoiceState> state, String currency, Long invoiceNumber ) {
	
	
	public double amount() {
		return Arrays.stream(this.items()).mapToDouble(InvoiceItem::amount).sum();
	}
	
	public double amountVAT() {
		return Arrays.stream(this.items()).mapToDouble(InvoiceItem::amountVAT).sum();
	}
	
	public double amountPlusVAT() {
		return Arrays.stream(this.items()).mapToDouble(InvoiceItem::amountPlusVAT).sum();
	}
	
	public LocalDateTime expiryDate() {
		return this.invoiceDate().plus(this.daysDue(), ChronoUnit.DAYS);
	}
	
	public static record RateOverAmount (double vatRate, double amount, double vatAmount) {
		public static RateOverAmount of(Map.Entry<Double,Double> entry) {
			return new RateOverAmount(entry.getKey(), entry.getValue(),  entry.getValue() * entry.getKey());
		}
	}
	
	public List<RateOverAmount> rateOverAmount() {
		Map<Double,Double> amountByRate = Stream.of(items).collect(Collectors.groupingBy(InvoiceItem::vatRate, Collectors.summingDouble(InvoiceItem::amount)));
		return amountByRate.entrySet().stream().sorted(Comparator.comparingDouble(Entry::getKey)).map(RateOverAmount::of).toList();
	}

}
