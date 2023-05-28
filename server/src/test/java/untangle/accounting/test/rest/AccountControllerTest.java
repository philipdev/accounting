package untangle.accounting.test.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import untangle.accounting.data.AccountData;
import untangle.accounting.data.AccountDetails;
import untangle.accounting.data.AccountEntryData;
import untangle.accounting.data.AccountType;
import untangle.accounting.server.rest.AccountController;
import untangle.accounting.server.service.AccountService;

@WebMvcTest
@ContextConfiguration(classes=AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTest {
	
	@MockBean
	AccountService service;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void testCreateAccount() throws Exception {
		String body = """
			{
				"accountType":"ASSET",
				"accountName": "1",
				"accountNumber": "1"
			}
		""";
		mockMvc.perform(post("/api/account").contentType("application/json").content(body)).andExpect(status().isOk());
	}
	
	@Test 
	void testGetAccount() throws Exception {
		when(service.getAccount(1L)).thenReturn(new AccountData(Optional.of(1L), AccountType.ASSET, "1", "1"));
		String expected = """
				{
					"id":1,
					"accountType":"ASSET",
					"accountName": "1",
					"accountNumber": "1"
				}
			""";
		mockMvc.perform(get("/api/account/1")).andExpect(content().json(expected));
	}
	
	@Test
	void testGetAccountDetails() throws Exception {
		AccountData account = new AccountData(Optional.of(1L), AccountType.ASSET, "1", "1");
		Double debit = 115.5d;
		Double credit = 0d;
		AccountEntryData[] entries = {
				new AccountEntryData(
						Optional.of(1L), 
						1L, 
						Optional.of(LocalDateTime.of(2024, 5, 28, 14, 00)), 
						115.5d, 
						0.0d, 
						Optional.of(""), 
						Optional.of(LocalDateTime.of(2024, 5, 28, 14, 00)))
		};
		
		when(service.getAccountDetails(1L)).thenReturn(new AccountDetails(account, entries, debit, credit));
	
		String expected = """
			{
			"account": {
				"id":1,
				"accountType":"ASSET",
				"accountName": "1",
				"accountNumber": "1"
			},
			"debit":115.5,
			"credit":0,
			"transactions":[
				{
					"id":1,
					"accountId":1,
					"executedAt": "2024-05-28T14:00:00",
					"debit":115.5,
					"credit":0,
					"description":"",
					"createdAt":"2024-05-28T14:00:00"
				}
			]
			}
		""";
		
		mockMvc.perform(get("/api/account/1/transaction")).andExpect(content().json(expected));
	}
	
	@Test
	void testGetAccounts() throws Exception {
		when(service.getAccounts()).thenReturn(List.of(
				new AccountData(Optional.of(1L), AccountType.ASSET, "1", "1"), 
				new AccountData(Optional.of(2L), AccountType.ASSET, "2", "2")
		));
		String expected = """
				[{
					"id":1,
					"accountType":"ASSET",
					"accountName": "1",
					"accountNumber": "1"
				},
				{
					"id":2,
					"accountType":"ASSET",
					"accountName": "2",
					"accountNumber": "2"
				}
				]
			""";
		mockMvc.perform(get("/api/account")).andExpect(content().json(expected));
	}
}
