package untangle.accounting.test.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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

import untangle.accounting.data.AccountData;
import untangle.accounting.data.AccountDetails;
import untangle.accounting.data.AccountEntryData;
import untangle.accounting.data.TransactionData;
import untangle.accounting.data.TransactionEntryData;
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
	void testCreateTransactionEmpty() throws Exception {
		String body = """
			{
				"executedAt":"2023-05-28T10:22:27.018Z",
				"entries": [],
				"description": ""	
			}
		""";
		mockMvc.perform(post("/api/account/transaction")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testCreateTransactionBasic() throws Exception {
		String body = """
			{
				"executedAt": "2023-05-28T10:22:27.018Z",
				"entries": [
					{
							"account": "100", 
							"debit": 100,
							"credit": 0
					},
					{
							"account": "200", 
							"debit": 0,
							"credit": 100
					}
				],
				"description": ""	
			}
		""";
		mockMvc.perform(post("/api/account/transaction")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		
		ArgumentCaptor<TransactionData> enterData = ArgumentCaptor.forClass(TransactionData.class);
		verify(service).createTransaction(enterData.capture());
		LocalDateTime executedAt = LocalDateTime.of(2023, 5, 28, 10, 22, 27, (int) TimeUnit.NANOSECONDS.convert(18, TimeUnit.MILLISECONDS));
		TransactionEntryData[] entries = {
				new TransactionEntryData("100", 100d, 0d),
				new TransactionEntryData("200", 0d, 100d),
		};
	
		TransactionData actual = enterData.getValue();
		assertThat(actual.executedAt()).isEqualTo(executedAt);
		assertThat(actual.entries()).isEqualTo(entries);
		assertThat(actual.description()).hasValue("");
	}
	
	@Test
	void testGetAccountDetails() throws Exception {
		String expected = """
			{
				"account": "10",
				"transactions":[{
					"account":"10",
					"executedAt":"2023-05-28T10:22:27.018",
					"debit":100.0,
					"credit":0.0,
					"description":"zzz",
					"createdAt":"2023-05-28T10:22:27.018"
				}],
				"debit":100.0,
				"credit":0.0
			}
		""";
		LocalDateTime executedAt = LocalDateTime.of(2023, 5, 28, 10, 22, 27, (int) TimeUnit.NANOSECONDS.convert(18, TimeUnit.MILLISECONDS));
		
		AccountDetails details = new AccountDetails("10", new AccountEntryData[] {new AccountEntryData("10", Optional.of(executedAt), 100d, 0d, Optional.of("zzz"), Optional.of(executedAt))}, 100d, 0d);
		when(service.getAccountDetails("10")).thenReturn(details);
		
		mockMvc.perform(get("/api/account/10")).andExpect(content().json(expected));
	}

}
