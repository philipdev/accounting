package untangle.accounting.server.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.ISpringTemplateEngine;

import com.itextpdf.html2pdf.HtmlConverter;

import jakarta.transaction.Transactional;
import untangle.accounting.data.CompanyData;
import untangle.accounting.data.InvoiceData;
import untangle.accounting.data.InvoiceDownloadData;
import untangle.accounting.data.InvoiceItem;
import untangle.accounting.data.OwnerCompanyData;
import untangle.accounting.data.TransactionData;
import untangle.accounting.data.TransactionEntryData;
import untangle.accounting.server.entity.Company;
import untangle.accounting.server.entity.CompanyDetails;
import untangle.accounting.server.entity.Invoice;
import untangle.accounting.server.entity.InvoiceEntry;
import untangle.accounting.server.entity.InvoiceState;
import untangle.accounting.server.repository.CompanyRepository;
import untangle.accounting.server.repository.InvoiceRepository;

@Service
public class InvoiceService {
	
	private InvoiceRepository invoiceRepository;
	private CompanyRepository companyRepository;
	private ISpringTemplateEngine templateEngine;
	private AccountService accountService;
	
	public InvoiceService(InvoiceRepository invoiceRepository, CompanyRepository companyRepository,
						  AccountService accountService, ISpringTemplateEngine templateEngine) {
		super();
		this.invoiceRepository = invoiceRepository;
		this.companyRepository = companyRepository;
		this.accountService = accountService;
		this.templateEngine = templateEngine;
	}

	public static CompanyDetails toCompanyDetails(CompanyData companyData) {
		return new CompanyDetails(companyData.vat(), companyData.name(), companyData.address(), companyData.city(), companyData.zipCode(), companyData.country(),companyData.contact(), companyData.phoneNumber(), companyData.email());
	}
	
	public static CompanyData toCompanyData(Long id, CompanyDetails details, Optional<OwnerCompanyData> ownerData) {
		return new CompanyData(Optional.ofNullable(id), details.getVat(), details.getName(), details.getAddress(), details.getCity(), details.getZipCode(), details.getCountry(), details.getContact(), details.getPhoneNumber(), details.getEmail(), false, ownerData);
	}
	
	public static InvoiceItem toInvoiceItem(InvoiceEntry entry) {
		return new InvoiceItem(entry.getDescription(), entry.getReferenceId(), entry.getVatRate(), entry.getCount(), entry.getUnitPrice());
	}
	
	public static InvoiceEntry toInvoiceEntry(InvoiceItem item) {
		return new InvoiceEntry(item.reference(), item.count(), item.unitPrice(), item.vatRate(), item.description());
	}
	
	public static InvoiceItem[] toInvoiceItems(List<InvoiceEntry> entries) {
		return entries.stream().map(InvoiceService::toInvoiceItem).toArray(InvoiceItem[]::new);
	}
	
	public static InvoiceData toInvoiceData(Invoice invoice) {
		CompanyData from = toCompanyData(invoice.getFromId(), invoice.getFromDetails(), Optional.of(new OwnerCompanyData(invoice.getIban(), invoice.getBic(), invoice.getJurisdiction())));
		CompanyData to = toCompanyData(invoice.getToId(), invoice.getToDetails(), Optional.empty());
		return new InvoiceData(Optional.ofNullable(invoice.getId()), to, Optional.ofNullable(from), invoice.getInvoiceDate(), invoice.getPaymentPeriod().getDays(), toInvoiceItems(invoice.getEntries()), invoice.getReferenceId(), invoice.getCustomerReferenceId(), Optional.of(invoice.getState()), invoice.getCurrency(), invoice.getInvoiceNumber());
	}
	
	@Transactional
	public InvoiceData createInvoice(InvoiceData invoiceData) throws FailedToGeneratedReceipt {
		Company owner = companyRepository.findByOwnerTrue().orElseThrow(()->new OwnerCompanyDoesNotExistException());
		CompanyDetails from = owner.getDetails();
		CompanyDetails to = toCompanyDetails(invoiceData.invoiceTo());
		
		List<InvoiceEntry> entries = Stream.of(invoiceData.items()).map(InvoiceService::toInvoiceEntry).toList();
		
		Invoice invoice = new Invoice(from, to, invoiceData.invoiceDate(), Period.ofDays(invoiceData.daysDue()), invoiceData.reference(), invoiceData.description(), entries);
		invoice.setState(InvoiceState.POSTED);
		invoice.setInvoiceNumber(invoiceRepository.getNextInvoiceNumber().orElse(1L));
		invoice.setIban(owner.getIban());
		invoice.setBic(owner.getBic());
		invoice.setJurisdiction(owner.getJurisdiction());
		generateReceipt(invoice);
		
		InvoiceData result= toInvoiceData(invoiceRepository.save(invoice));
		
		
		TransactionEntryData[] accountEntries = { 
				new TransactionEntryData("400", result.amountPlusVAT(), 0d),
				new TransactionEntryData("704", result.amount(), 0d),
				new TransactionEntryData("451", 0d, result.amountVAT())
		};
		
		accountService.addTransaction(new TransactionData(result.invoiceDate(), accountEntries, Optional.of("Invoice " + result.invoiceNumber())));
	
		return result;
	}

	private void generateReceipt(Invoice invoice) throws FailedToGeneratedReceipt {	
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Context context = new Context(Locale.forLanguageTag(invoice.getLocale()));
	        context.setVariable("invoice", toInvoiceData(invoice));	        
	        String html = templateEngine.process("view/invoice.html", context); 
	        HtmlConverter.convertToPdf(html, out);
            
            invoice.setGeneratedInvoice(BlobProxy.generateProxy(out.toByteArray()));
		} catch(Exception e) {
			throw new FailedToGeneratedReceipt(e);
		}
	}
	
	public List<InvoiceData> getInvoices() {
		var result = invoiceRepository.findAll();
		try(var s = StreamSupport.stream(result.spliterator(), false)) {
			return s.map(InvoiceService::toInvoiceData).toList();
		}
	}

	public InvoiceData getInvoice(Long id) {
		Optional<Invoice> invoice = invoiceRepository.findById(id);
		return invoice.map(InvoiceService::toInvoiceData).orElseThrow(() -> new NotFoundException("Invoice with id '%s' not found".formatted(id)));
	}
	
	@Transactional
	public InvoiceDownloadData getInvoiceAsPDF(Long id) throws IOException, SQLException {
		Invoice invoice = invoiceRepository.findById(id).orElseThrow();
		String from = invoice.getFromDetails().getName();
		String to = invoice.getToDetails().getName();
		
		String filename = "%06d_%s_%s_%s.pdf".formatted(invoice.getInvoiceNumber(), invoice.getInvoiceDate().toLocalDate() ,from, to );
		Blob html = invoice.getGeneratedInvoice();
		byte[] data= html.getBytes(1L, (int) html.length());
		return new InvoiceDownloadData(filename, new ByteArrayInputStream(data));
	}
	
}
