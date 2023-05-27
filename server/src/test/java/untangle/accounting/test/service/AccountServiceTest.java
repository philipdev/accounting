package untangle.accounting.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import untangle.accounting.data.AccountData;
import untangle.accounting.data.AccountDetails;
import untangle.accounting.data.AccountEntryData;
import untangle.accounting.data.AccountType;
import untangle.accounting.data.DebitCredit;
import untangle.accounting.server.entity.Account;
import untangle.accounting.server.entity.AccountEntry;
import untangle.accounting.server.repository.AccountEntryRepository;
import untangle.accounting.server.repository.AccountRepository;
import untangle.accounting.server.service.AccountService;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {
	
	@Mock
	AccountRepository accountRepository;
	
	@Mock
	AccountEntryRepository accountEntryRepository;
	
	@InjectMocks
	AccountService service;
	
	
	@Test
	void testCreateAccount() {
		AccountData accountData = new AccountData(Optional.empty(), AccountType.ASSET, "name", "number");
	
		when(accountRepository.save(any(Account.class))).thenAnswer(args -> {
			Account account = args.getArgument(0);
			ReflectionTestUtils.setField(account, "id", Long.valueOf(1));
			return account;
		});
		
		service.createAccount(accountData);
		
		verify(accountRepository).save(any(Account.class));
	}
	
	@Test
	void testGetAccounts() {
		service.getAccounts();
		verify(accountRepository).findAll();
	}
	
	@Test
	void testCreateEntry() {
		Account account = new Account(AccountType.EQUITY, "1", "1");
		ReflectionTestUtils.setField(account, "id", Long.valueOf(1));
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		
		when(accountEntryRepository.save(any(AccountEntry.class))).thenAnswer(args -> {
			AccountEntry entry = args.getArgument(0);
			ReflectionTestUtils.setField(entry, "id", Long.valueOf(1));
			return entry;
		});
		
		AccountEntryData entryData = new AccountEntryData(Optional.empty(), 1L, Optional.empty(), 0D, 0D, Optional.of(""), Optional.empty());
		service.createEntry(entryData);
		verify(accountEntryRepository).save(any(AccountEntry.class));
	}
	
	@Test
	void testGetAccountDetails() {
		Account account = new Account(AccountType.EQUITY, "1", "1");
		ReflectionTestUtils.setField(account, "id", Long.valueOf(1));
		//Account account, LocalDateTime executedAt, Double debit, Double credit, String description
		List<AccountEntry> entries = List.of(new AccountEntry(account, LocalDateTime.now(), 100D, 0D, ""), new AccountEntry(account, LocalDateTime.now(), 100D,0D,""));
		ReflectionTestUtils.setField(entries.get(0), "id", Long.valueOf(1));
		ReflectionTestUtils.setField(entries.get(1), "id", Long.valueOf(2));
		
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		when(accountEntryRepository.findAllByAccount(any(Account.class))).thenReturn(entries);
		when(accountEntryRepository.calculateDebitCreditByAccount(any(Account.class))).thenReturn(Optional.of(new DebitCredit(1L, 200D,0D)));
		
		AccountDetails details = service.getAccountDetails(1L);
		assertThat(details.debit()).isEqualTo(200D);
		assertThat(details.credit()).isEqualTo(0D);
		assertThat(details.account().id()).hasValue(1L);
	}

}
