import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, Validators, FormArray} from '@angular/forms';
import { AccountService, AccountData} from '../account.service';


@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
}) 
export class CreateAccountComponent {
  
  @Input()
  embedded = false;
  
  accountForm = this.fb.group({
	accountType: ['', Validators.required],
    accountName: ['', Validators.required],
    accountNumber: ['', Validators.required],
  });

  @Output() accountAdded = new EventEmitter<AccountData>();
 
  constructor(private fb: FormBuilder, private accountService:AccountService) { }
  
  onSubmit() {
	let accountData = this.accountForm.value as AccountData;
	this.accountService.createAccount(accountData).subscribe(()=>this.accountAdded.emit(accountData),(e)=>console.warn(e));
  }
}


 