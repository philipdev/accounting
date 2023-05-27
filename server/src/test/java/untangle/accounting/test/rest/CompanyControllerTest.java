package untangle.accounting.test.rest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import untangle.accounting.data.CompanyData;
import untangle.accounting.server.rest.CompanyController;
import untangle.accounting.server.service.AlreadyExistsException;
import untangle.accounting.server.service.CompanyService;

@WebMvcTest(controllers = CompanyController.class)
@ContextConfiguration(classes=CompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CompanyControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CompanyService service;
	
	@Test
	void getCompanies() throws Exception {
		List<CompanyData> list = new ArrayList<>();
		list.add(new CompanyData(Optional.of(0L),"vat","name", "address", "city", "zipCode", "country", "contact", "phoneNumber", "email", false,Optional.empty()));
		when(service.getCompanies()).thenReturn(list);
		mockMvc.perform(get("/api/company")).andExpect(content().json("[{\"id\":0,\"vat\":\"vat\",\"name\":\"name\",\"address\":\"address\",\"city\":\"city\",\"zipCode\":\"zipCode\",\"country\":\"country\",\"contact\":\"contact\",\"phoneNumber\":\"phoneNumber\",\"email\":\"email\",\"owner\":false,\"ownerData\":null}]"));
	}
	
	@Test
	void getCompaniesEmptyList() throws Exception {
		List<CompanyData> list = new ArrayList<>();
		when(service.getCompanies()).thenReturn(list);
		mockMvc.perform(get("/api/company")).andExpect(content().json("[]"));
	}
	
	@Test
	void createCompany() throws Exception {
		CompanyData expected = new CompanyData(Optional.of(0L),"vat","name", "address", "city", "zipCode", "country", "contact", "phoneNumber", "email@host", false,Optional.empty());
		mockMvc.perform(post("/api/company")
				.content("{\"id\":0,\"vat\":\"vat\",\"name\":\"name\",\"address\":\"address\",\"city\":\"city\",\"zipCode\":\"zipCode\",\"country\":\"country\",\"contact\":\"contact\",\"phoneNumber\":\"phoneNumber\",\"email\":\"email@host\",\"owner\":false}")
				.contentType("application/json")).andExpect(status().isOk());			
		verify(service,times(1)).createCompany(eq(expected));
	}
	
	@Test 
	void deleteCompanies() throws Exception {
		mockMvc.perform(delete("/api/company")
				.content("[1,3,5]")
				.contentType("application/json"));
		verify(service,times(1)).deleteByIds(List.of(1L,3L,5L));
	}
	
	@Test
	void checkNameExists() throws Exception {
		doThrow(AlreadyExistsException.class).when(service).validateName("NAME", null);
		mockMvc.perform(get("/api/company/validate/name/NAME")).andExpect(status().isConflict());	
	}
	
	@Test
	void checkVatExists() throws Exception {
		doThrow(AlreadyExistsException.class).when(service).validateVAT("VAT", null);
		mockMvc.perform(get("/api/company/validate/vat/VAT")).andExpect(status().isConflict());	
	}
	
	@Test
	void checkVatNotExists() throws Exception {
		doNothing().when(service).validateVAT("VAT", null);
		mockMvc.perform(get("/api/company/validate/vat/VAT")).andExpect(status().isOk());	
	}
	
	@Test
	void checkNotNotExists() throws Exception {
		doNothing().when(service).validateName("name", null);
		mockMvc.perform(get("/api/company/validate/name/name")).andExpect(status().isOk());	
	}
	
}
