package untangle.accounting.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import untangle.accounting.data.CompanyData;
import untangle.accounting.data.OwnerCompanyData;
import untangle.accounting.server.entity.Company;
import untangle.accounting.server.repository.CompanyRepository;

@Service
public class CompanyService {
	private CompanyRepository companyRepository;
	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}
	
	@Transactional
	public CompanyData createCompany(CompanyData companyData) {
	
		validateIfOwnerNotPresent(companyData);
		validateVAT(companyData.vat(), null);
		validateName(companyData.name(), null);
		Company company = toCompany(companyData);
		return toCompanyData(companyRepository.save(company));
	}

	private void validateIfOwnerNotPresent(CompanyData companyData) {
		if(companyData.owner() && companyRepository.findByOwnerTrue().isPresent()) {
			throw new OwnerCompanyAlreadyExistsException();
		}
	}
	
	@Transactional 
	public CompanyData updateCompany(Long id, CompanyData companyData) {
		Company company = companyRepository
				.findById(id)
				.orElseThrow(() -> new NotFoundException("Company does not exist"));
		
		validateUniqueOwner(companyData, company);	
		validateVAT(companyData.vat(), id);
		validateName(companyData.name(), id);
		
		updateCompany(company, companyData);
		
		return toCompanyData(companyRepository.save(company));
	}

	private void validateUniqueOwner(CompanyData companyData, Company company) {
		if(!company.isOwner() && companyData.owner()) {
			Optional<Company> existingOwner = companyRepository.findByOwnerTrue();
			if(existingOwner.isPresent()) {
				throw new OwnerCompanyAlreadyExistsException();
			}
		}
	}

	private void updateCompany(Company company, CompanyData companyData) {
		company.setVat(companyData.vat());
		company.setName(companyData.name());
		company.setAddress(companyData.address());
		company.setCity(companyData.city());
		company.setZipCode(companyData.zipCode());
		company.setCountry(companyData.country());
		company.setContact(companyData.contact());
		company.setPhoneNumber(companyData.phoneNumber());
		company.setEmail(companyData.email());
		company.setOwner(companyData.owner());
		
		if(companyData.owner()) {
			company.setIban(companyData.ownerData().get().iban());
			company.setBic(companyData.ownerData().get().bic());
			company.setJurisdiction(companyData.ownerData().get().jurisdiction());
		}
	}
	
	public static Company toCompany(CompanyData companyData) {
		Company company = new Company(companyData.vat(), companyData.name(), companyData.address(), companyData.city(), companyData.zipCode(), companyData.country(),companyData.contact(), companyData.phoneNumber(), companyData.email(), companyData.owner());
		if(companyData.owner()) {
			company.setIban(companyData.ownerData().get().iban());
			company.setBic(companyData.ownerData().get().bic());
			company.setJurisdiction(companyData.ownerData().get().jurisdiction());
		}
		return company;
	}
	
	public static CompanyData toCompanyData(Company company) {
		Optional<OwnerCompanyData> ownerData;
		if(company.isOwner()) {
			ownerData = Optional.of(new OwnerCompanyData(company.getIban(), company.getBic(), company.getJurisdiction()));
		} else {
			ownerData = Optional.empty();
		}
		return new CompanyData(Optional.of(company.getId()), company.getVat(), company.getName(), company.getAddress(), company.getCity(), company.getZipCode(), company.getCountry(),company.getContact(), company.getPhoneNumber(), company.getEmail(), company.isOwner(), ownerData);
	}

	public List<CompanyData> getCompanies() {
		try(var s = StreamSupport.stream(companyRepository.findAll().spliterator(), false)) {
			return s.map(CompanyService::toCompanyData).toList();
		}
	}
	
	@Transactional
	public void deleteByIds(List<Long> ids) {
		companyRepository.deleteAllById(ids);
	}

	public void validateVAT(String vat, Long currentCompanyId) {
		validateNotExists(companyRepository.findByDetailsVat(vat), currentCompanyId);	
	}

	public void validateName(String name, Long currentCompanyId) {
		validateNotExists(companyRepository.findByDetailsName(name), currentCompanyId);		
	}
	
	private static void validateNotExists(Optional<Company> company, Long currentCompanyId) {
		boolean exists = company
				.filter(comp-> !comp.getId().equals(currentCompanyId))
				.isPresent();
		if(exists) {
			throw new AlreadyExistsException();
		}
	}
	

}
