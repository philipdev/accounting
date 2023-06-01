package untangle.accounting.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import untangle.accounting.data.AccountData;
import untangle.accounting.data.AccountDetails;
import untangle.accounting.data.DebitCredit;
import untangle.accounting.data.TransactionData;
import untangle.accounting.data.TransactionEntryData;
import untangle.accounting.server.entity.AccountEntry;
import untangle.accounting.server.repository.AccountEntryRepository;
import untangle.accounting.server.service.AccountService;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {
	
	
	@Mock
	AccountEntryRepository accountEntryRepository;
	
	@InjectMocks
	AccountService service;
	
	@Test
	void testAccountDetails() {
		AccountEntry[] entries = {
				new AccountEntry("100", 0L, LocalDateTime.now(), 100d, 0d, ""),
				new AccountEntry("100", 0L, LocalDateTime.now(), 150d, 0d, ""),
				new AccountEntry("100", 0L, LocalDateTime.now(), 0d, 100d, ""),
		};
		ReflectionTestUtils.setField(entries[0], "id", Long.valueOf(1));
		ReflectionTestUtils.setField(entries[1], "id", Long.valueOf(2));
		ReflectionTestUtils.setField(entries[2], "id", Long.valueOf(3));
		
		when(accountEntryRepository.findAllByAccount("100")).thenReturn(List.of(entries));
		
		AccountDetails details = service.getAccountDetails("100");
		assertThat(details.account()).isEqualTo("100");
		assertThat(details.debit()).isEqualTo(250d);
		assertThat(details.credit()).isEqualTo(100d);
	}
	
	@Test
	void testCreateTransaction() {
		LocalDateTime executedAt = LocalDateTime.of(2023, 5, 28, 10, 22, 27, (int) TimeUnit.NANOSECONDS.convert(18, TimeUnit.MILLISECONDS));
		TransactionEntryData[] entries = {
				new TransactionEntryData("100", 100d, 0d),
				new TransactionEntryData("101", 0d, 100d),
		};
		TransactionData trx = new TransactionData(executedAt, entries, Optional.of("transfer"));
		service.createTransaction(trx);
		ArgumentCaptor<AccountEntry> args = ArgumentCaptor.forClass(AccountEntry.class);
		verify(accountEntryRepository, times(2)).save(args.capture());
		List<AccountEntry> actualEntries = args.getAllValues();
		assertThat(actualEntries.get(0)).hasFieldOrPropertyWithValue("executedAt", executedAt);
		assertThat(actualEntries.get(0)).hasFieldOrPropertyWithValue("debit", 100d);
		assertThat(actualEntries.get(1)).hasFieldOrPropertyWithValue("executedAt", executedAt);
		assertThat(actualEntries.get(1)).hasFieldOrPropertyWithValue("credit", 100d);
	}
	
	@Test
	void testDebitCreditByAccount() {
		String account = "A";
		List<AccountEntry> entries = List.of(
				new AccountEntry("AB", 	1L, LocalDateTime.now(), 133.3d, 0d, ""),
				new AccountEntry("AC", 	1L, LocalDateTime.now(), 0d, 100d, ""),
				new AccountEntry("ABC", 1L, LocalDateTime.now(), 0d, 250d, ""),
				new AccountEntry("A", 	1L, LocalDateTime.now(), 100d, 0d, "")
		);
		
		when(accountEntryRepository.findAllByAccount(account)).thenReturn(entries);
		
		AccountDetails details = service.getAccountDetails(account);
		assertThat(details.debit()).isEqualTo(233.3d);
		assertThat(details.credit()).isEqualTo(350d);
		assertThat(details.account()).isEqualTo(account);
	}
}
