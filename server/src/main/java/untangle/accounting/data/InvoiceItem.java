package untangle.accounting.data;


public record InvoiceItem (String description, String reference, double vatRate, double count, double unitPrice)  {
	
	
	public double amountPlusVAT() {
		return amount() + amountVAT();
	}
	
	public double amount() {
		return this.count() * this.unitPrice();
	}
	
	public double amountVAT() {
		return amount() * vatRate();
	}
	
}
