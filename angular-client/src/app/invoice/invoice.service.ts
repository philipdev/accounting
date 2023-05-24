import { Injectable } from '@angular/core';
import { CompanyData} from '../company/company.service';
import { Observable } from 'rxjs';
import { HttpClient } from "@angular/common/http";
import { AppConfigService } from '../app-config.service';
import { HttpCrudServiceBase, FetchStatus } from '../common/http-crud-service.base';

const DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

@Injectable({
  providedIn: 'root'
})
export class InvoiceService extends HttpCrudServiceBase<InvoiceData>{
	
	
	constructor(http:HttpClient, private config:AppConfigService) { 
		super(config.getServerURL() + '/invoice', http);
		this.fetch();
	}
  
    public getInvoices(): Observable<InvoiceData[]> {
		return this.getList();
	}
  
	public createInvoice(data:InvoiceData):Observable<InvoiceData> {
		return this.create(data);
	} 

	public getPdfLink(id:number):string {
		return this.config.getServerURL() + '/invoice/download/' + id + '.pdf';
	}
	
	public amountExcludingVAT(invoice:InvoiceData) {
		return invoice.items.reduce((total:number, e:InvoiceItem)=> total + (e.count * e.unitPrice) , 0);
	}
	
	public amountVAT(invoice:InvoiceData) {
		return invoice.items.reduce((total:number, e:InvoiceItem)=> total + (e.count * e.unitPrice * e.vatRate) , 0);
	}
	
	public amountIncludingVAT(invoice:InvoiceData) {
		return this.amountExcludingVAT(invoice) + this.amountVAT(invoice);
	}
	
	public getExpiryDate(invoice:InvoiceData) : Date {
		const invoiceDate = new Date(invoice.invoiceDate)
		return new Date(invoiceDate.getTime() + invoice.daysDue * DAY_IN_MILLIS);
	}
	
}



export interface InvoiceData {
  id?:number;
  invoiceFrom?:CompanyData;
  invoiceTo:CompanyData;
  invoiceDate: string;
  items:InvoiceItem[];
  daysDue:number;
  reference:string;
  description:string;
  invoiceNumber:number;
}

export interface InvoiceItem {
	description:string;
	reference:string;
	vatRate:number;
	count:number;
	unitPrice:number;
}