import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators,AsyncValidator, FormArray, AbstractControl, ValidatorFn, ValidationErrors} from '@angular/forms';
import { CompanyData, CompanyService, OwnerData} from '../company.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subject, of, map, catchError, take } from 'rxjs';
import { requiredIfValidator, Predicate } from '../../common/validators';

@Component({
  selector: 'app-update-company',
  templateUrl: './update-company.component.html',
  styleUrls: ['./update-company.component.scss']
})
export class UpdateCompanyComponent implements OnInit {
	 
	 router = inject(Router);
	 activatedroute = inject(ActivatedRoute);
	 
	 fb = inject(FormBuilder);
	 companyService = inject(CompanyService);
	 id = parseInt(this.activatedroute.snapshot.params["id"]);
	 
	 companyForm = this.fb.group({
		name: ['', Validators.required, this.companyService.nameValidator(this.id)],
		vat: ['', Validators.required, , this.companyService.vatValidator(this.id)],
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
	
	company = this.companyService.getCompany(this.id);
	
	ngOnInit() {
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
		
		this.companyService.status$
			.pipe(take(1))
			.subscribe(s => {
			
			this.companyForm.disable();
			this.company.subscribe((comp)=> { 
				this.companyForm.patchValue(comp);
				
				this.companyForm.enable();
				//this.checkOwner();
			});
		});
		this.companyService.fetch();
	}
	checkOwner() {
		const owner = this.companyForm.get('owner') as AbstractControl;
		const ownerData = this.companyForm.get('ownerData') as AbstractControl;
		
		if(owner!=null && owner.value) {
			ownerData.enable();
		} else {
			ownerData.disable();
		}
	}
	
	requiredIfOwner() {
		return requiredIfValidator(()=> {
			const owner = this.companyForm.get('owner') as AbstractControl;
			if(owner!=null) {
				return owner.value;
			} else {
				return false;
			}
		});
	}
	
	public onSubmit() {
		const company = this.companyForm.value as CompanyData;
		this.companyService.update(this.id, company).subscribe( (updated)=> this.router.navigateByUrl('/company/list'));
	}
}
