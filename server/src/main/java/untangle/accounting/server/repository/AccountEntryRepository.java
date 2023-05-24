package untangle.accounting.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import untangle.accounting.data.DebitCredit;
import untangle.accounting.server.entity.Account;
import untangle.accounting.server.entity.AccountEntry;

public interface AccountEntryRepository extends CrudRepository<AccountEntry, Long>{
	public Iterable<AccountEntry> findAllByAccount(Account account);
	
	@Query("SELECT new untangle.accounting.data.DebitCredit(t.account.id,SUM(t.debit),SUM(t.credit)) FROM AccountEntry t GROUP BY(t.account)")
	public Iterable<DebitCredit> calculateDebitCreditGroupByAccount();
	
	@Query("SELECT new untangle.accounting.data.DebitCredit(t.account.id,SUM(t.debit),SUM(t.credit)) FROM AccountEntry t  WHERE t.account=?1 GROUP BY(t.account)")
	public Optional<DebitCredit> calculateDebitCreditByAccount(Account account);
	
}
