import { Component, OnDestroy } from '@angular/core';
import { CompanyData, CompanyService} from '../company.service';
import { FormBuilder, FormGroup, FormControl, FormArray} from '@angular/forms';
import { Subject, take, takeUntil } from 'rxjs';
import { MatCheckboxChange } from '@angular/material/checkbox';
@Component({
  selector: 'app-list',
  templateUrl: './list-company.component.html',
  styleUrls: ['./list-company.component.scss']
})
export class ListCompanyComponent implements OnDestroy {
	displayedColumns = ["select","name","vat", "address", "contact", "owner"];
	
	destroy$: Subject<boolean> = new Subject<boolean>();
	
	companies$ = this.companyService.getCompanies().pipe(takeUntil(this.destroy$));
	
	formGroup = this.fb.group({
		selected: this.fb.array([])
	});
	
	all = this.fb.control(false);

	
	constructor(private fb:FormBuilder , private companyService:CompanyService) {
		this.companies$.subscribe((companies:CompanyData[]) => {
			const formArray = this.formGroup.get('selected') as FormArray;
			
			formArray.clear();
			companies.forEach(comp=> formArray.push(this.fb.group({ 
				selected: [false],
				id: [comp]
			})));
		});
		
		this.all.valueChanges.subscribe((checked:any)=> 
			this.processSelectables((check,comp) => check.setValue(checked))
		);
		companyService.fetch();
	}
	
	public getControl(i:number):FormControl {
		const formArray = this.formGroup.get('selected') as FormArray;
		const formGroup = formArray.at(i) as FormGroup;
		return formGroup.get('selected') as FormControl;
	}
	
	private processSelectables(callback:forEachSelectable) {
		const formArray = this.formGroup.get('selected') as FormArray;
		
		for(let i=0; i< formArray.length; i++) {
			const formGroup = formArray.at(i) as FormGroup;
			const selected = formGroup.get('selected') as FormControl;
			const company = formGroup.get('id') as FormControl;
			callback(selected, company);
		}
	}
	
	public deleteSelected() {
		const ids:number[] = [];
		this.processSelectables((check,comp) => {
			if(check.value)  {
				ids.push(comp.value.id);
			}
		});
		this.companyService.deleteByIds(ids).subscribe(()=>console.log('deleted'));
	}
	
	public get hasSelected() {
		const ids:number[] = [];
		this.processSelectables((check,comp) => {
			if(check.value)  {
				ids.push(comp.value.id);
			}
		});
		return ids.length > 0;
	}
	public selectRow(id:number) {
		this.processSelectables((check,comp) => {
			check.setValue(!check.value);
		});
	}
	
	    

    ngOnDestroy() {
        this.destroy$.next(true);
        this.destroy$.unsubscribe();
    }

}

type forEachSelectable = (checkControl: FormControl, companyControl: FormControl) => void;
