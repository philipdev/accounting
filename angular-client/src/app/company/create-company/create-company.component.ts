import { Component, Injectable } from '@angular/core';

import { FormBuilder, Validators, AsyncValidatorFn, FormArray, AbstractControl, ValidatorFn, ValidationErrors} from '@angular/forms';
import { CompanyData, CompanyService, OwnerData} from '../company.service';
import { Router } from '@angular/router';
import { Observable, Subject, of, map, catchError, tap} from 'rxjs';
import { requiredIfValidator, Predicate } from '../../common/validators';
@Component({
  selector: 'app-create',
  templateUrl: './create-company.component.html',
  styleUrls: ['./create-company.component.scss']
})




export class CreateCompanyComponent {
	
	constructor(private fb: FormBuilder, private companyService: CompanyService, private router: Router) {
		const ownerData = this.companyForm.get('ownerData');
		const owner = this.companyForm.get('owner');
		if(ownerData == null) return;
		if(owner == null) return;
		owner.valueChanges.subscribe((v:any)=> {
			if(v) {
				ownerData.enable();
			} else {
				ownerData.disable();
			}
			this.companyForm.updateValueAndValidity();
		});
	}
	
  	companyForm = this.fb.group({
		name: ['', Validators.required,  this.companyService.nameValidator(null)],
		vat: ['', Validators.required, this.companyService.vatValidator(null)],
		address: ['', Validators.required],
		city: ['', Validators.required],
		zipCode: ['', Validators.required],
		country: ['', Validators.required],
		contact: ['', Validators.required],
		phoneNumber: ['', Validators.required],
		email: ['', [Validators.required,Validators.email]],
		owner: [false, Validators.required],
		ownerData: this.fb.group({
			iban: ['', this.requiredIfOwner()],
			bic: ['', this.requiredIfOwner()],
			jurisdiction: ['', this.requiredIfOwner()],
		})
	});
  
    
	public onSubmit() {
		const value = Object.assign({}, this.companyForm.value);
		const name =  this.companyForm.get('name');
		const vat = this.companyForm.get('vat');
		
		if(name === null || vat === null) return;
		const nameValue = name.value;
		const vatValue = vat.value;
		if(typeof  nameValue === 'string' && typeof  vatValue === 'string') {
			const data = Object.assign(value, {name:nameValue, vat:vatValue}) as CompanyData;
			this.companyService.createCompany(data).subscribe((created)=> this.router.navigateByUrl('/company/list'));
		}
	}
  
	requiredIfOwner() {
		return requiredIfValidator(()=> {
			const owner = this.companyForm.get('owner') as AbstractControl;
			if(owner!=null) return owner.value;
			else return false;
		});
	}
  
  
}






