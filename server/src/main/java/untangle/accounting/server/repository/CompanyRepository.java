package untangle.accounting.server.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import untangle.accounting.server.entity.Company;

public interface CompanyRepository extends CrudRepository<Company,Long>{
	Optional<Company> findByOwnerTrue();
	
	Iterable<Company> findAllByOwnerFalse();
	
	Optional<Company> findByDetailsVat(String vat);
	
	Optional<Company> findByDetailsName(String name);
}
