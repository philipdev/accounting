package untangle.accounting.data;

import java.io.InputStream;

public record InvoiceDownloadData(String filename, InputStream download) {
	
	
}
