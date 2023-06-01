package untangle.accounting.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import untangle.accounting.data.CompanyData;
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
	void testNameNotExists() {
		when(repo.findByDetailsName(eq("name"))).thenReturn(Optional.empty());
		assertThatNoException().isThrownBy(()-> service.validateName("name", null));
	}
	
	@Test
	void testVatNotExists() {
		when(repo.findByDetailsVat(eq("vat"))).thenReturn(Optional.empty());
		assertThatNoException().isThrownBy(()-> service.validateVAT("vat", null));
	}
	
	@Test
	void testNameNotExistsForUpdate() {
		Company company = new Company("vat","name","address","city","zip","country","contact","phone","email",false);
		company.setId(1L);
		
		when(repo.findByDetailsName(eq("name"))).thenReturn(Optional.of(company));
		assertThatNoException().isThrownBy(()-> service.validateName("name", 1L));
	}
	
	@Test
	void testVatNotExistsForUpdate() {
		Company company = new Company("vat","name","address","city","zip","country","contact","phone","email",false);
		company.setId(1L);
		
		when(repo.findByDetailsVat(eq("vat"))).thenReturn(Optional.of(company));
		assertThatNoException().isThrownBy(()-> service.validateVAT("vat", 1L));
	}
	
	@Test
	void testVatExistsForUpdate() {
		Company company = new Company("vat","name","address","city","zip","country","contact","phone","email",false);
		company.setId(199L);
		
		when(repo.findByDetailsVat(eq("vat"))).thenReturn(Optional.of(company));
		assertThatThrownBy(()-> service.validateVAT("vat", 1L));
	}
	
	@Test
	void testNameExistsForUpdate() {
		Company company = new Company("vat","name","address","city","zip","country","contact","phone","email",false);
		company.setId(2L);
		
		when(repo.findByDetailsName(eq("name"))).thenReturn(Optional.of(company));
		assertThatThrownBy(()-> service.validateName("name", 1L));
	}
	
	@Test
	void createCompany() {
		CompanyData companyData = new CompanyData(
				Optional.empty(), "BE0000000000", "Company name", "Company address", "Company city",
				"0000", "Belgium", "Jane Do", "0495000000", "email@host", false, Optional.empty()
		);
		when(repo.save(any(Company.class))).thenAnswer(args -> { 
				Company company = (Company) args.getArgument(0);
				company.setId(1L);
				return company; 
		});		
		
		assertThat(service.createCompany(companyData)).hasFieldOrPropertyWithValue("name", "Company name");	
	}
}
