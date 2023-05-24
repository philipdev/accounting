package untangle.accounting.test.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import untangle.accounting.data.AccountType;
import untangle.accounting.server.DatabaseConfiguration;
import untangle.accounting.server.entity.Account;
import untangle.accounting.server.repository.AccountRepository;


@DataJpaTest(showSql = true)
@ActiveProfiles("test")
@ContextConfiguration(classes = {DatabaseConfiguration.class})
public class AccountRepositoryTest {
	
	@Autowired
	AccountRepository repo;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	void createAccount() {
		Account account = new Account(AccountType.ASSET, "abc", "123");
		Long id = repo.save(account).getId();
		testEntityManager.flush();
		assertThat(repo.findById(id)).contains(account);	
	}
	
	@Test 
	void duplicatedAccountNumber() {
		Account account1 = new Account(AccountType.LIABILITY, "1234567890", "abc");
		Account account2 = new Account(AccountType.ASSET, "1234567890", "xyz");
		repo.save(account1);
		repo.save(account2);
		
		assertThatThrownBy(()-> testEntityManager.flush());
	}
}
