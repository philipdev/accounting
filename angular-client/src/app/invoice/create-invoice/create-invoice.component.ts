import { Component, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { FormBuilder, Validators, FormGroup, FormArray, NgForm} from '@angular/forms';
import { Router } from '@angular/router';
import { CompanyData, CompanyService} from '../../company/company.service';
import { MatSelectChange } from '@angular/material/select';
import { InvoiceItem, InvoiceData, InvoiceService } from '../invoice.service';

@Component({
  selector: 'app-create-invoice',
  templateUrl: './create-invoice.component.html',
  styleUrls: ['./create-invoice.component.scss']
})
export class CreateInvoiceComponent {
	constructor(private fb: FormBuilder, private invoiceService:InvoiceService, private companyService:CompanyService, private router: Router) {
		this.addItem();
		companyService.fetch();
		invoiceService.fetch();
	}
	
	invoiceForm = this.fb.group({
		items: this.fb.array([],Validators.required),
		invoiceDate: [new Date().toISOString(), Validators.required],
		daysDue: [30, Validators.required],
		description: [''],
		reference: [''],
		invoiceTo:this.fb.group({
			name: ['', Validators.required, this.companyService.nameValidator(null)],
			vat: ['', Validators.required,  this.companyService.vatValidator(null)],
			address: ['', Validators.required],
			city: ['', Validators.required],
			zipCode: ['', Validators.required],
			country: ['', Validators.required],
			contact: ['', Validators.required],
			phoneNumber: ['', Validators.required],
			email: ['', [Validators.required, Validators.email]]
		})
	});
	
	public get companies() {
	  return this.companyService.getCompanies();
	}
	
	public get invoiceToForm() {
		return this.invoiceForm.get('invoiceTo') as FormGroup;
	}
	
	
	public get items() {
		return this.invoiceForm.get('items') as FormArray;
	}
	
	public get itemControls() {
		return this.items.controls as FormGroup[];
	}
	
	public addItem() {
		console.log('addItem');
		const itemForm = this.fb.group({
			description: ['',  Validators.required],
			vatRate: [0.21, Validators.required],
			count: [1,  Validators.required],
			unitPrice: [0, Validators.required]
		});
		this.items.push(itemForm);
	}
	
	public removeItem(i:number) {
		this.items.removeAt(i);
	}
	
	public get totalPrice() {
		return this.items.value.reduce((total:number, e:InvoiceItem)=> total + (e.count * e.unitPrice) , 0);
	}
	
	public get totalVAT() {
		return this.items.value.reduce((total:number, e:InvoiceItem)=> total + (e.count * e.unitPrice * e.vatRate) , 0);
	}
	
	public get totalPriceIncludingVAT() {
		return this.totalPrice + this.totalVAT;
	}
	
	public invoiceToSelectChanged(event:MatSelectChange) {
		console.log('invoiceToSelectChanged', event.value);
		this.invoiceToForm.patchValue(event.value);
		
		this.invoiceToForm.get('name')?.setAsyncValidators([this.companyService.nameValidator(event.value.id)]);
		this.invoiceToForm.get('vat')?.setAsyncValidators([this.companyService.vatValidator(event.value.id)]);
		
		this.invoiceToForm.get('name')?.updateValueAndValidity();
		this.invoiceToForm.get('vat')?.updateValueAndValidity();
	}
	
	public get invoiceTo() {
		return this.invoiceForm.value.invoiceTo;
	}
	
	public onSubmit() {
		this.invoiceService.createInvoice(this.invoiceForm.value as InvoiceData).subscribe((created)=> this.router.navigateByUrl('/invoice/list'));
	}
}

