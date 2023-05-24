import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { InvoiceItem, InvoiceData, InvoiceService } from '../invoice.service';

@Component({
  selector: 'app-invoice-detail',
  templateUrl: './invoice-detail.component.html',
  styleUrls: ['./invoice-detail.component.scss']
})
export class InvoiceDetailComponent {
	router = inject(Router);
	activatedroute = inject(ActivatedRoute);
	invoiceService = inject(InvoiceService);
	id = parseInt(this.activatedroute.snapshot.params["id"]);
	invoice$ =  this.invoiceService.get(this.id);
	
	

}
