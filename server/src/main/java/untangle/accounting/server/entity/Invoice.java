package untangle.accounting.server.entity;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;

@Entity
public class Invoice {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Version
	private long version;
	
	private Long fromId;

	@Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "vat", column = @Column(name = "from_vat")),    
        @AttributeOverride(name = "name", column = @Column(name = "from_name")),
        @AttributeOverride(name = "address", column = @Column(name = "from_address")),
        @AttributeOverride(name = "city", column = @Column(name = "from_city")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "from_zipCode")),
        @AttributeOverride(name = "country", column = @Column(name = "from_country")),
        @AttributeOverride(name = "contact", column = @Column(name = "from_contact")),
        @AttributeOverride(name = "phoneNumber", column = @Column(name = "from_phoneNumber")),
        @AttributeOverride(name = "email", column = @Column(name = "from_email")),
        @AttributeOverride(name = "iban", column = @Column(name = "from_iban")),
    })
	private CompanyDetails from;
	
	private Long toId;
	@Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "vat", column = @Column(name = "to_vat")),   
        @AttributeOverride(name = "name", column = @Column(name = "to_name")),
        @AttributeOverride(name = "address", column = @Column(name = "to_address")),
        @AttributeOverride(name = "city", column = @Column(name = "to_city")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "to_zipCode")),
        @AttributeOverride(name = "country", column = @Column(name = "to_country")),
        @AttributeOverride(name = "contact", column = @Column(name = "to_contact")),
        @AttributeOverride(name = "phoneNumber", column = @Column(name = "to_phoneNumber")),
        @AttributeOverride(name = "email", column = @Column(name = "to_email")),
        @AttributeOverride(name = "iban", column = @Column(name = "to_iban")),
    })
	private CompanyDetails to;
	
	@Column(unique = true)
	private Long invoiceNumber;
	
	@CreatedDate
	private LocalDateTime creationDate;
	private LocalDateTime invoiceDate;
	private Integer paymentPeriod;
	
	private String referenceId;
	private String description;
	private InvoiceState state = InvoiceState.DRAFT;
	
	private String iban;
	private String bic;
	private String jurisdiction;
	
	private String currency = "EUR";
	private String locale = "nl-BE";
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "invoice")
	
	private List<InvoiceEntry> entries = new ArrayList<>();
	
	@Lob
    private Blob generatedInvoice;
    
	public Long getId() {
		return id;
	}
	
	public Invoice() {
		
	}
	public Invoice(CompanyDetails from, CompanyDetails to, LocalDateTime invoiceDate, Period paymentPeriod,
			String referenceId, String description, List<InvoiceEntry> entries) {
		super();
		this.from = from;
		this.to = to;
		this.invoiceDate = invoiceDate;
		this.paymentPeriod = paymentPeriod.getDays();
		this.referenceId = referenceId;
		this.description = description;
		entries.forEach((entry)-> entry.setInvoice(this));
		this.entries = entries;
	}
	
	public void setGeneratedInvoice(Blob blob) {
		this.generatedInvoice = blob;
	}
	
	public List<InvoiceEntry> getEntries() {
		if(state == InvoiceState.DRAFT) {
			return entries;
		} else {
			return Collections.unmodifiableList(entries);
		}
	}

	public CompanyDetails getFromDetails() {
		return from;
	}
	
	public CompanyDetails getToDetails() {
		return to;
	}

	public Long getFromId() {
		return fromId;
	}
	
	public Long getToId() {
		return toId;
	}

	public LocalDateTime getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDateTime invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Period getPaymentPeriod() {
		return Period.ofDays(paymentPeriod) ;
	}

	public void setPaymentPeriod(Period paymentPeriod) {
		this.paymentPeriod = paymentPeriod.getDays();
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getCustomerReferenceId() {
		return description;
	}

	public void setCustomerReferenceId(String customerReferenceId) {
		this.description = customerReferenceId;
	}

	public void setEntries(List<InvoiceEntry> entries) {
		this.entries = entries;
	}
	public void setState(InvoiceState state) {
		this.state = state;
	}
	public InvoiceState getState() {
		return state;
	}

	public Blob getGeneratedInvoice() {
		return generatedInvoice;
	}

	public String getCurrency() {
		return currency;
	}
	
	public Long getInvoiceNumber() {
		return invoiceNumber;
	}
	
	public void setInvoiceNumber(Long number) {
		this.invoiceNumber = number;
	}


	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}



	
	
}
