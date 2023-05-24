package untangle.accounting.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	CompanyDetails details;
	private boolean owner;
	
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	private String iban;
	private String bic;
	private String jurisdiction;
	
	
	private Company() {

	}

	public Company(String vat, String name, String address, String city, String zipCode, String country,
			String contact, String phoneNumber, String email, boolean owner) {
		details = new CompanyDetails(vat, name, address, city, zipCode, country, contact, phoneNumber, email);
		this.owner = owner;
	}

	public String getVat() {
		return details.getVat();
	}

	public void setVat(String vat) {
		details.setVat(vat);
	}

	public String getName() {
		return details.getName();
	}

	public void setName(String name) {
		details.setName(name);
	}

	public String getAddress() {
		return details.getAddress();
	}

	public void setAddress(String address) {
		details.setAddress(address);
	}

	public String getCity() {
		return details.getCity();
	}

	public void setCity(String city) {
		details.setCity(city);
	}

	public String getZipCode() {
		return details.getZipCode();
	}

	public void setZipCode(String zipCode) {
		details.setZipCode(zipCode);
	}

	public String getCountry() {
		return details.getCountry();
	}

	public void setCountry(String country) {
		details.setCountry(country);
	}
	public void setContact(String contact) {
		details.setContact(contact);
	}
	public String getPhoneNumber() {
		return details.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		details.setPhoneNumber(phoneNumber);
	}

	public String getEmail() {
		return details.getEmail();
	}

	public void setEmail(String email) {
		details.setEmail(email);
	}

	public boolean equals(Object obj) {
		return details.equals(obj);
	}

	public void setId(Long id) {
		this.id = id;
		
	}

	public Long getId() {
		return id;
	}

	public boolean isOwner() {
		return owner;
	}
	
	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	public CompanyDetails getDetails() {
		return details;
	}

	public String getContact() {
		return details.getContact();
	}


}
