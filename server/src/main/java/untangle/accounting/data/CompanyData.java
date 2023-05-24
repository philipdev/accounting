package untangle.accounting.data;

import java.util.Optional;

public record CompanyData(Optional<Long> id, String vat, String name, String address, String city, String zipCode, String country, String contact, 
		String phoneNumber, String email, boolean owner, Optional<OwnerCompanyData> ownerData) {

}
