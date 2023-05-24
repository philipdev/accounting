package untangle.accounting.server.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class InvoiceEntry {
	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getVatRate() {
		return vatRate;
	}

	public void setVatRate(Double vat) {
		this.vatRate = vat;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="invoice_id")
    private Invoice invoice;
	
	private String referenceId;
	private Double unitPrice;
	private Double count;
	private String description;
	private Double vatRate;

	
	public InvoiceEntry() {
		
	}
	
	public InvoiceEntry(String referenceId, double count, double amount, double vatRate, String description) {
		this.referenceId = referenceId;
		this.unitPrice = amount;
		this.count = count;
		this.vatRate = vatRate;
		this.description = description;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(referenceId,unitPrice,count);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof InvoiceEntry other ) {
			return Objects.equals(referenceId, other.referenceId) &&
					Objects.equals(unitPrice, other.unitPrice) &&
					Objects.equals(count, other.count);
		} else {
			return false;
		}
	}

	public Long getId() {
		return id;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	

}
