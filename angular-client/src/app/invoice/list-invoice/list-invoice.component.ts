import { Component } from '@angular/core';
import { InvoiceItem, InvoiceData, InvoiceService } from '../invoice.service';
import { CompanyData, CompanyService} from '../../company/company.service';
@Component({
  selector: 'app-list-invoice',
  templateUrl: './list-invoice.component.html',
  styleUrls: ['./list-invoice.component.scss']
})
export class ListInvoiceComponent {
	displayedColumns = ['invoiceNumber','invoiceDate', 'amount','company', 'contact', 'pdf'];
	invoices$ = this.invoiceService.getInvoices();
	
	constructor(public invoiceService:InvoiceService, public companyService:CompanyService) {
		invoiceService.fetch();
		companyService.fetch();
	}
	
	public pdfLink(id:number):string {
		return this.invoiceService.getPdfLink(id);
	}
	
	public getInvoiceAmount(invoice: InvoiceData):number {
		return this.invoiceService.amountIncludingVAT(invoice);
	}
	
	public get hasOwner() {
		return this.companyService.hasOwner;
	}
}
