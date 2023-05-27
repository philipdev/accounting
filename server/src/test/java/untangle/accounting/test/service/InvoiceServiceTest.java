package untangle.accounting.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import untangle.accounting.data.CompanyData;
import untangle.accounting.data.InvoiceData;
import untangle.accounting.data.InvoiceItem;
import untangle.accounting.server.repository.CompanyRepository;
import untangle.accounting.server.repository.InvoiceRepository;
import untangle.accounting.server.service.FailedToGeneratedReceipt;
import untangle.accounting.server.service.InvoiceService;
import untangle.accounting.server.service.OwnerCompanyDoesNotExistException;

@ExtendWith(SpringExtension.class)
public class InvoiceServiceTest {
	
	@Mock
	InvoiceRepository invoiceRepository;
	
	@Mock
	CompanyRepository companyRespository;
	
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
}
