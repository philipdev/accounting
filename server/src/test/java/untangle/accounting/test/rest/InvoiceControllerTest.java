package untangle.accounting.test.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

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
}
