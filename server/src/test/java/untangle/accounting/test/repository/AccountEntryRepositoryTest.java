package untangle.accounting.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import untangle.accounting.data.DebitCredit;
import untangle.accounting.server.DatabaseConfiguration;
import untangle.accounting.server.entity.AccountEntry;
import untangle.accounting.server.repository.AccountEntryRepository;

@DataJpaTest(showSql = true)
@ActiveProfiles("test")
@ContextConfiguration(classes = {DatabaseConfiguration.class})
public class AccountEntryRepositoryTest {
	
	@Autowired
	AccountEntryRepository repository;
	
	@Test
	void testFindSubAccount() {
		
		repository.save(new AccountEntry("AB", 1L, LocalDateTime.now(), 0d, 0d, ""));
		repository.save(new AccountEntry("AC", 1L, LocalDateTime.now(), 0d, 0d, ""));
		repository.save(new AccountEntry("ABC", 1L, LocalDateTime.now(), 0d, 0d, ""));
		repository.save(new AccountEntry("X", 1L, LocalDateTime.now(), 0d, 0d, ""));
		repository.save(new AccountEntry("A", 1L, LocalDateTime.now(), 0d, 0d, ""));
		
		Iterable<String> result = repository.findUniqueAccountsOrdered("A%");
		assertThat(result).isEqualTo(List.of("A", "AB", "ABC", "AC"));
	}
	

}
