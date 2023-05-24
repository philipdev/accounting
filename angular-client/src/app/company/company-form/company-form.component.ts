import { Component, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { FormBuilder, Validators, FormArray, FormGroup} from '@angular/forms';
import { CompanyData} from '../company.service';

@Component({
  selector: 'app-company-form',
  templateUrl: './company-form.component.html',
  styleUrls: ['./company-form.component.scss']
})
export class CompanyFormComponent {
	
	constructor(private fb: FormBuilder) {
	}
	
	@Input()
	companyFormGroup!:FormGroup;

}
