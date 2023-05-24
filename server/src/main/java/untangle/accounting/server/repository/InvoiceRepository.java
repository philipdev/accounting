package untangle.accounting.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import untangle.accounting.server.entity.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice,Long>{
	
	@Query("SELECT MAX(i.invoiceNumber) + 1 from Invoice i")
	Optional<Long> getNextInvoiceNumber();

}
