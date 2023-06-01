package untangle.accounting.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring6.ISpringTemplateEngine;

import untangle.accounting.data.CompanyData;
import untangle.accounting.data.InvoiceData;
import untangle.accounting.data.InvoiceItem;
import untangle.accounting.data.TransactionData;
import untangle.accounting.data.TransactionEntryData;
import untangle.accounting.server.entity.Company;
import untangle.accounting.server.entity.Invoice;
import untangle.accounting.server.entity.InvoiceState;
import untangle.accounting.server.repository.CompanyRepository;
import untangle.accounting.server.repository.InvoiceRepository;
import untangle.accounting.server.service.AccountService;
import untangle.accounting.server.service.FailedToGeneratedReceipt;
import untangle.accounting.server.service.InvoiceService;
import untangle.accounting.server.service.OwnerCompanyDoesNotExistException;

@ExtendWith(SpringExtension.class)
public class InvoiceServiceTest {
	
	@Mock
	ISpringTemplateEngine templateEngine;
	
	@Mock
	InvoiceRepository invoiceRepository;
	
	@Mock
	CompanyRepository companyRespository;
	
	@Mock
	AccountService accountService;
	
	@InjectMocks
	InvoiceService service;

	@Test()
	void testCreateInvoiceNoOwner() throws FailedToGeneratedReceipt {
		
		LocalDateTime invoiceDate = LocalDateTime.of(2023, 1, 1, 0, 0);
		int daysDue = 30;
		InvoiceItem[] items = new InvoiceItem[0];
		String reference = "ref";
		String description = "desc";
		String currency = "EUR";
		Long invoiceNumber = 1L;
		CompanyData invoiceTo = new CompanyData(
				Optional.empty(), 
				"BE0000000000", 
				"Company name", 
				"Company address", 
				"Company city",
				"0000", 
				"Belgium", 
				"Jane Do", 
				"0495000000", 
				"email@host", 
				false, 
				Optional.empty()
		);
		InvoiceData invoiceData = new InvoiceData(Optional.empty(), invoiceTo, Optional.empty(), invoiceDate, daysDue, items, reference, description, Optional.empty(), currency, invoiceNumber);
		when(companyRespository.findByOwnerTrue()).thenReturn(Optional.empty());
		
		Throwable thrownException = catchThrowable(() -> service.createInvoice(invoiceData));
		assertThat(thrownException).isInstanceOf(OwnerCompanyDoesNotExistException.class);
	}
	
	@Test()
	void testCreateInvoiceOk() throws FailedToGeneratedReceipt {
		LocalDateTime invoiceDate = LocalDateTime.of(2023, 1, 1, 0, 0);
		int daysDue = 30;
		InvoiceItem[] items = { 
				new InvoiceItem("desc", "ref", 0.21, 1, 1000)
		};
		String reference = "ref";
		String description = "desc";
		String currency = "EUR";
		Long invoiceNumber = 1L;

		CompanyData invoiceTo = new CompanyData(
				Optional.empty(), 
				"BE0000000000", 
				"Company name", 
				"Company address", 
				"Company city",
				"0000", 
				"Belgium", 
				"Jane Do", 
				"0495000000", 
				"email@host", 
				false, 
				Optional.empty()
		);
		InvoiceData invoiceData = new InvoiceData(Optional.empty(), invoiceTo, Optional.empty(), invoiceDate, daysDue, items, reference, description, Optional.empty(), currency, invoiceNumber);
		Company ownerCompany = new Company("vat","name","address","city","zip","country","contact","phone","email",false);
		ownerCompany.setOwner(true);
		ownerCompany.setBic("BIC");
		ownerCompany.setIban("IBAN");
		ownerCompany.setJurisdiction("Jurisdiction");
		when(companyRespository.findByOwnerTrue()).thenReturn(Optional.of(ownerCompany));
		when(templateEngine.process(anyString(), any(IContext.class))).thenReturn("<html>invoice data</html>");
		when(invoiceRepository.save(any(Invoice.class))).thenAnswer(args -> { 
			Invoice invoice = (Invoice) args.getArgument(0);
			ReflectionTestUtils.setField(invoice, "id", Long.valueOf(1));
			return invoice; 
		});
		when(invoiceRepository.getNextInvoiceNumber()).thenReturn(Optional.of(3L));
		
		InvoiceData created = service.createInvoice(invoiceData);
				
		assertThat(created.state()).hasValue(InvoiceState.POSTED);
		assertThat(created.invoiceNumber()).isEqualTo(3L);
		assertThat(created.amountPlusVAT()).isEqualTo(1210d);
		
		ArgumentCaptor<TransactionData> trxCapture = ArgumentCaptor.forClass(TransactionData.class);
		verify(accountService).addTransaction(trxCapture.capture());
		TransactionEntryData[] journalEntries = { 
				new TransactionEntryData("400", 1210d, 0d), // debtor account
				new TransactionEntryData("704", 1000d, 0d), // profit
				new TransactionEntryData("451", 0d, 210d)   // owed VAT
		};
		
		assertThat(trxCapture.getValue().entries()).hasSameElementsAs(List.of(journalEntries));
		
	}
}
