package untangle.accounting.test.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import untangle.accounting.server.entity.Company;
import untangle.accounting.server.repository.CompanyRepository;
import untangle.accounting.server.service.CompanyService;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {
	
	@Mock
	CompanyRepository repo;
	
	@InjectMocks
	CompanyService service;
	
	@Test
	void testNameExists() {
		Company company = new Company("vat","name","address","city","zip","country","contact","phone","email",false);
		when(repo.findByDetailsName(eq("name"))).thenReturn(Optional.of(company));
				
		assertThatThrownBy(()-> service.validateName("name", null));
	}
	@Test
	void testVatExists() {
		when(repo.findByDetailsVat(eq("vat"))).thenReturn(Optional.empty());
		assertThatNoException().isThrownBy(()-> service.validateVAT("vat", null));
	}
	
	@Test
	void testVatNotExists() {
		when(repo.findByDetailsName(eq("name"))).thenReturn(Optional.empty());
		assertThatNoException().isThrownBy(()-> service.validateName("name", null));
	}
	
}
