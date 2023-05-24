package untangle.accounting.server.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import untangle.accounting.data.AccountData;
import untangle.accounting.data.AccountDetails;
import untangle.accounting.data.DebitCredit;
import untangle.accounting.data.AccountEntryData;
import untangle.accounting.server.entity.Account;
import untangle.accounting.server.entity.AccountEntry;
import untangle.accounting.server.repository.AccountRepository;
import untangle.accounting.server.repository.AccountEntryRepository;

@Service
public class AccountService {
	
	private AccountRepository accountRepository;
	private AccountEntryRepository transactionRepository;
	
	public AccountService(AccountRepository repository, AccountEntryRepository transactionRepository) {
		this.accountRepository = repository;
		this.transactionRepository = transactionRepository;
	}
	
	@Transactional
	public AccountData createAccount(AccountData data) {
		Account account = new Account(data.accountType(),  data.accountNumber(), data.accountName());
		
		return toAccountData(accountRepository.save(account));
	}

	public List<AccountData> getAccounts() {
		try(var s = StreamSupport.stream(accountRepository.findAll().spliterator(), false)) {
			return s.map(AccountService::toAccountData).sorted(AccountService::compareAccount).toList();
		}
	}
	
	private static int compareAccount(AccountData a, AccountData b) {
		int value = a.accountType().compareTo(b.accountType());
		return value == 0 ? a.accountNumber().compareTo(b.accountNumber()) : value;		
	}
	
	@Transactional
	public AccountEntryData creatEntry(AccountEntryData transactionData) {
		Account account = accountRepository.findById(transactionData.accountId()).orElseThrow(()-> new NotFoundException("Account with id '%s' not found!".formatted(transactionData.accountId()))); // throw exception with response code
		LocalDateTime executedAt = transactionData.executedAt().orElse(LocalDateTime.now());
		String description = transactionData.description().orElse("");
		AccountEntry trx = new AccountEntry(account, executedAt, transactionData.debit(), transactionData.credit(), description);
		return toTransactionData(transactionRepository.save(trx));
	}
	
	public AccountDetails getAccountDetails(Long accountId) {
		Account account = accountRepository.findById(accountId).orElseThrow(()-> new NotFoundException("Account with id '%s' not found!".formatted(accountId)));
		DebitCredit dc = transactionRepository.calculateDebitCreditByAccount(account).orElse(new DebitCredit(account.getId(), 0d, 0d));
		Iterable<AccountEntry> result = transactionRepository.findAllByAccount(account);
		
		try(var s = StreamSupport.stream(result.spliterator(), false)) {
			AccountEntryData[] trxList = s.map(AccountService::toTransactionData ).toArray((size)-> new AccountEntryData[size]);	
			return new AccountDetails(toAccountData(account),trxList , dc.debit(),dc.credit());
		}
	}
	
	public static AccountEntryData toTransactionData(AccountEntry trx) {
		return new AccountEntryData(Optional.of(trx.getId()), trx.getAccount().getId(), Optional.of(trx.getExecutedAt()), trx.getDebit(), trx.getCredit(), Optional.ofNullable(trx.getDescription()), Optional.ofNullable(trx.getCreatedAt()));
	}
	
	public AccountData getAccount(Long accountId) {
		return accountRepository.findById(accountId).map(AccountService::toAccountData).get() ;
	}
	
	public static AccountData toAccountData(Account a) {
		return new AccountData(Optional.of(a.getId()),a.getType(), a.getName(), a.getNumber());
	}
}
