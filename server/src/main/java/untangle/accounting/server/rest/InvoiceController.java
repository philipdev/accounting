package untangle.accounting.server.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import untangle.accounting.data.InvoiceData;
import untangle.accounting.data.InvoiceDownloadData;
import untangle.accounting.server.service.FailedToGeneratedReceipt;
import untangle.accounting.server.service.InvoiceService;

@RestController
@RequestMapping("/api")
public class InvoiceController {
	
	private InvoiceService invoiceService;
	
	public InvoiceController(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}

	@PostMapping("/invoice")
	public InvoiceData createInvoice(@RequestBody InvoiceData invoiceData) throws FailedToGeneratedReceipt {
		return invoiceService.createInvoice(invoiceData);
	}
	
	@GetMapping("/invoice")
	public List<InvoiceData> getInvoices() {
		return invoiceService.getInvoices();
	}
	
	@GetMapping(value="/invoice/download/{id}.pdf")
	public ResponseEntity<InputStreamResource> getSavedInvoice(@PathVariable("id") Long id) throws IOException, SQLException {
		InvoiceDownloadData download = invoiceService.getInvoiceAsPDF(id);
		return ResponseEntity.ok()
				  .header("Content-Disposition", "attachment; filename=%s".formatted(download.filename()))
			      .contentType(MediaType.APPLICATION_OCTET_STREAM)
			      .body(new InputStreamResource(download.download()));
	}
	
}
