package untangle.accounting.server.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import untangle.accounting.data.CompanyData;
import untangle.accounting.server.service.CompanyService;

@RestController
@RequestMapping("/api")
public class CompanyController {
	
	private CompanyService companyService;

	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}
	
	@PostMapping("/company")
	public CompanyData createCompany(@RequestBody CompanyData companyData) {
		return this.companyService.createCompany(companyData);
	}
	
	@PostMapping("/company/{id}")
	public CompanyData updateCompany(@PathVariable("id") Long id, @RequestBody CompanyData companyData) {
		return this.companyService.updateCompany(id, companyData);
	}
	
	@GetMapping("/company") 
	public List<CompanyData> getCompanies() {
		return this.companyService.getCompanies();
	}
	
	@DeleteMapping("/company")
	public void deleteCompaniesById(@RequestBody List<Long> ids) {
		this.companyService.deleteByIds(ids);
	}
	
	@GetMapping("/company/validate/vat/{vat}")
	public void validateVAT(@PathVariable("vat") String vat, @RequestParam(name="current", required = false) Long id) {
		this.companyService.validateVAT(vat, id);
	}
	
	@GetMapping("/company/validate/name/{name}")
	public void validateCompanyName(@PathVariable("name") String name, @RequestParam(name="current", required = false) Long id) {
		this.companyService.validateName(name, id);
	}
	
}
