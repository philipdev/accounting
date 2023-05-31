package untangle.accounting.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.StreamSupport;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import untangle.accounting.data.AccountData;
import untangle.accounting.data.AccountDetails;
import untangle.accounting.data.AccountEntryData;
import untangle.accounting.data.DebitCredit;
import untangle.accounting.data.TransactionData;
import untangle.accounting.server.entity.AccountEntry;
import untangle.accounting.server.repository.AccountEntryRepository;

@Service
public class AccountService {
	
	private Random random = new Random();
	
	
	private AccountEntryRepository accountEntryRepository;
	
	public AccountService( AccountEntryRepository accountEntryRepository) {
		this.accountEntryRepository = accountEntryRepository;
	}
	
	public List<AccountData> getAccounts() {
		Iterable<String> result = accountEntryRepository.findAllAccounts();
		try(var s = StreamSupport.stream(result.spliterator(), false);) {
			return s.map(AccountData::new).toList();
		}
	}
	
	public AccountDetails getAccountDetails(String account) {
		Iterable<AccountEntry> result = accountEntryRepository.findAllByAccount(account);
		DebitCredit dc = calculateDebitCredit(account, result);
		
		try(var s = StreamSupport.stream(result.spliterator(), false);) {
			AccountEntryData[] trxList = s. map(AccountService::toTransactionData ).toArray((size)-> new AccountEntryData[size]);	
			return new AccountDetails(account, trxList , dc.debit(), dc.credit());
		}
	}

	private DebitCredit calculateDebitCredit(String account, Iterable<AccountEntry> result) {
		double totalDebit = 0;
		double totalCredit = 0;
		for (AccountEntry e: result) {
			totalDebit += e.getDebit();
			totalCredit += e.getCredit();
		}
		DebitCredit dc = new DebitCredit(account, totalDebit, totalCredit);
		return dc;
	}
	
	public static AccountEntryData toTransactionData(AccountEntry trx) {
		return new AccountEntryData(trx.getAccount(), Optional.of(trx.getExecutedAt()), trx.getDebit(), trx.getCredit(), Optional.ofNullable(trx.getDescription()), Optional.ofNullable(trx.getCreatedAt()));
	}
	
	public static AccountData toAccountData(String account) {
		return new AccountData(account);
	}
	
	@Transactional
	public void createTransaction(TransactionData trxData) {
		Long refId = random.nextLong();
		
		 Arrays.stream(trxData.entries())
				.map(trxEntry -> new AccountEntry(trxEntry.account(), refId, trxData.executedAt(), trxEntry.debit(), trxEntry.credit(), trxData.description().orElse("")))
				.forEach(entity -> accountEntryRepository.save(entity));
		
	}
	
}
