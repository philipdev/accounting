package untangle.accounting.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import untangle.accounting.data.DebitCredit;
import untangle.accounting.server.entity.AccountEntry;

public interface AccountEntryRepository extends CrudRepository<AccountEntry, Long>{
	
	@Query("SELECT e FROM AccountEntry e WHERE e.account LIKE ?1% ORDER BY e.executedAt")
	public Iterable<AccountEntry> findAllByAccount(String account);
	
	@Query("SELECT new untangle.accounting.data.DebitCredit(t.account,SUM(t.debit),SUM(t.credit)) FROM AccountEntry t GROUP BY(t.account)")
	public Iterable<DebitCredit> calculateDebitCreditGroupByAccount();
	
	@Query("SELECT new untangle.accounting.data.DebitCredit(t.account,SUM(t.debit),SUM(t.credit)) FROM AccountEntry t  WHERE t.account=?1 GROUP BY(t.account)")
	public Optional<DebitCredit> calculateDebitCreditByAccount(String account);
	
	@Query("SELECT distinct(t.account) FROM AccountEntry t ORDER BY account")
	public Iterable<String> findAllAccounts();
	
	@Query("SELECT distinct(t.account) FROM AccountEntry t WHERE t.account LIKE ?1% ORDER BY t.account")
	public Iterable<String> findUniqueAccountsOrdered(String account);
}
