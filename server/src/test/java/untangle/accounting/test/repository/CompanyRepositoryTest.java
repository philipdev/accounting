package untangle.accounting.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import untangle.accounting.server.DatabaseConfiguration;
import untangle.accounting.server.entity.Company;
import untangle.accounting.server.repository.CompanyRepository;

@DataJpaTest(showSql = true)
@ActiveProfiles("test")
@ContextConfiguration(classes = {DatabaseConfiguration.class})
public class CompanyRepositoryTest {
	
	@Autowired
	CompanyRepository repository;
	
	@Test
	void testExistsVAT() {
		Company company = new Company("vat","name","address","city","zip","country","contact","phone","email",false);
		repository.save(company);
		assertThat(repository.findByDetailsVat("vat")).isPresent();
	}
	
	@Test
	void testExistsName() {
		Company company = new Company("vat","name","address","city","zip","country","contact","phone","email",false);
		repository.save(company);	
		assertThat(repository.findByDetailsName("name")).isPresent();
	}
	
	@Test
	void testNotExistsName() {
		assertThat(repository.findByDetailsName("unexisting vat")).isNotPresent();
	}
	
	@Test
	void testNotExistsVat() {
		assertThat(repository.findByDetailsVat("unexisting vat")).isNotPresent();
	}
}
