package untangle.accounting.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import untangle.accounting.server.DatabaseConfiguration;
import untangle.accounting.server.entity.Amount;
import untangle.accounting.server.entity.Invoice;
import untangle.accounting.server.entity.InvoiceEntry;
import untangle.accounting.server.repository.InvoiceRepository;

@DataJpaTest(showSql = true)
@ActiveProfiles("test")
@ContextConfiguration(classes = {DatabaseConfiguration.class})
public class InvoiceRepositoryTest {
	
	@Autowired
	InvoiceRepository repository;
	
	@Test
	public void invoiceEntries() {
		Invoice invoice = new Invoice();
		List<InvoiceEntry> entries = List.of(new InvoiceEntry("",1, 1,1,""), new InvoiceEntry("", 2, 2,1,""));
		entries.forEach((e)-> invoice.getEntries().add(e));
		Long id = repository.save(invoice).getId();
		
		Invoice found = repository.findById(id).get();
		assertThat(found.getEntries()).containsExactlyElementsOf(entries);
		
		InvoiceEntry insertEntry = new InvoiceEntry("", 3, 3,1,"");
		found.getEntries().add(1, insertEntry);
		repository.save(found);
		
		Invoice found2 = repository.findById(id).get();
		assertThat(found2.getEntries().get(1)).isEqualTo(insertEntry);
	}
}
