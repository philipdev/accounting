import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators, AsyncValidatorFn, FormGroup, FormArray, AbstractControl, ValidatorFn, ValidationErrors} from '@angular/forms';
import { AccountService, AccountData, TransactionData} from '../account.service';

@Component({
  selector: 'app-create-transaction',
  templateUrl: './create-transaction.component.html',
  styleUrls: ['./create-transaction.component.scss']
})
export class CreateTransactionComponent {
	
	router = inject(Router);
	fb = inject(FormBuilder);	
	accountService = inject(AccountService);
	trxForm = this.fb.group({
		entries: this.fb.array([], Validators.required),
		executedAt: [new Date().toISOString(), Validators.required],
		description: [''],
	});
	
	constructor() {
		this.addRow();
	}
	
	public get entries() {
		return this.trxForm.get('entries') as FormArray;
	}
	
	public addRow() {
		console.log('addItem');
		const left = this.fb.group({
			account: ['',  Validators.required],
			debit: [null as null|number, Validators.required],
			credit: [null as null|number, Validators.required]
		});
		const right = this.fb.group({
			account: ['',  Validators.required],
			debit: [null as null|number, Validators.required],
			credit: [null as null|number, Validators.required]
		});
		this.entries.push(left);
		this.entries.push(right);
	}
	
	public removeRow(i:number) {
		this.entries.removeAt(i);
		this.entries.removeAt(i);
	}
	
	public get entryControls() {
		return this.entries.controls as FormGroup[];
	}
		
	onSubmit() {
		const data = this.trxForm.value as TransactionData;
		this.accountService.create(data).subscribe(()=>{
			this.router.navigateByUrl('/account/list')
		});
	}

}
