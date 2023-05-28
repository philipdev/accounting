package untangle.accounting.test.rest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import untangle.accounting.data.CompanyData;
import untangle.accounting.data.InvoiceData;
import untangle.accounting.data.InvoiceItem;
import untangle.accounting.data.OwnerCompanyData;
import untangle.accounting.server.entity.InvoiceState;
import untangle.accounting.server.rest.InvoiceController;
import untangle.accounting.server.service.InvoiceService;

@WebMvcTest
@ContextConfiguration(classes=InvoiceController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InvoiceControllerTest {
	@Autowired
	private MockMvc mock;
	
	@MockBean	
	InvoiceService service;
	
	@Test
	void testGetInvoicesEmptyList() throws Exception {
		when(service.getInvoices()).thenReturn(new ArrayList<>());
		mock.perform(get("/api/invoice")).andExpect(content().json("[]"));
	}
	
	
	@Test
	void testCreateInvoice() throws Exception {
		String body = """
		{
		  "items": [
		    {
		      "description": "TV",
		      "vatRate": 0.21,
		      "count": 1,
		      "unitPrice": 1015
		    }
		  ],
		  "invoiceDate": "2023-05-28T10:22:27.018Z",
		  "daysDue": 30,
		  "description": "TV",
		  "reference": "ORDER1",
		  "invoiceTo": {
		    "name": "The company",
		    "vat": "xx",
		    "address": "Somestreet 99",
		    "city": "Nowhere",
		    "zipCode": "1000",
		    "country": "Belgium",
		    "contact": "xx",
		    "phoneNumber": "xx",
		    "email": "xx@xx"
		  }
		}
		""";
			
		when(service.createInvoice(any(InvoiceData.class))).then(args -> {
			InvoiceData input = args.getArgument(0);
			CompanyData ownerCompany =  new CompanyData(
					Optional.of(1L), 
					"BE0000000000", 
					"Company name", 
					"Company address", 
					"Company city",
					"0000", 
					"Belgium", 
					"Jane Do", 
					"0495000000", 
					"email@host", 
					true, 
					Optional.of(new OwnerCompanyData("","",""))
			);
			//  int daysDue, InvoiceItem[] items, String reference, String description, Optional<InvoiceState> state, String currency, Long invoiceNumber
			return new InvoiceData(
				Optional.of(1L),
				input.invoiceTo(),
				Optional.of(ownerCompany),
				input.invoiceDate(),
				input.daysDue(),
				input.items(),
				input.reference(),
				input.description(),
				Optional.of(InvoiceState.POSTED),
				"EUR",
				1L
			);
		});
		String expected = """
		{
		  "id": 1,
		  "invoiceTo": {
		    "vat": "xx",
		    "name": "The company",
		    "address": "Somestreet 99",
		    "city": "Nowhere",
		    "zipCode": "1000",
		    "country": "Belgium",
		    "contact": "xx",
		    "phoneNumber": "xx",
		    "email": "xx@xx",
		    "owner": false
		  },
		  "invoiceFrom": {
		    "id": 1,
		    "vat": "BE0000000000",
		    "name": "Company name",
		    "address": "Company address",
		    "city": "Company city",
		    "zipCode": "0000",
		    "country": "Belgium",
		    "contact": "Jane Do",
		    "phoneNumber": "0495000000",
		    "email": "email@host",
		    "owner": true,
		    "ownerData": {
		      "iban": "",
		      "bic": "",
		      "jurisdiction": ""
		    }
		  },
		  "invoiceDate": "2023-05-28T10:22:27.018",
		  "daysDue": 30,
		  "items":[{"description":"TV","reference":null,"vatRate":0.21,"count":1.0,"unitPrice":1015.0}],
		  "reference": "ORDER1",
		  "description": "TV",
		  "state": "POSTED",
		  "currency": "EUR",
		  "invoiceNumber": 1
		}
		""";
		
		mock.perform(post("/api/invoice")
			.content(body)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(expected))
			.andExpect(status().isOk());

		
		ArgumentCaptor<InvoiceData> captor = ArgumentCaptor.forClass(InvoiceData.class);
		verify(service).createInvoice(captor.capture());
		
		InvoiceData invoiceInput = captor.getValue();
		assertThat(invoiceInput.invoiceDate()).isEqualTo(LocalDateTime.of(2023, 5, 28, 10, 22, 27, (int) TimeUnit.NANOSECONDS.convert(18, TimeUnit.MILLISECONDS)));

	}
	

}
