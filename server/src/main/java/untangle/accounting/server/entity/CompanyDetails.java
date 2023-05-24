package untangle.accounting.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CompanyDetails {
	private CompanyDetails() {
		
	}
	public CompanyDetails(String vat, String name, String address, String city, String zipCode, String country,
			String contact, String phoneNumber, String email) {
		super();
		this.vat = vat;
		this.name = name;
		this.address = address;
		this.city = city;
		this.zipCode = zipCode;
		this.country = country;
		this.contact = contact;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}
	@Column( unique = true, nullable = false)
	private String vat;
	@Column( unique = true, nullable = false)
	private String name;
	private String address;
	private String city;
	private String zipCode;
	private String country;
	private String contact;

	private String phoneNumber;
	private String email;
	

	
	public String getVat() {
		return vat;
	}
	public void setVat(String vat) {
		this.vat = vat;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}

	
	
}
