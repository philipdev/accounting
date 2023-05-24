import { Component } from '@angular/core';
import { AccountService, AccountData} from '../account.service';
import { FormBuilder, Validators, FormArray} from '@angular/forms';

@Component({
  selector: 'app-list-accounts',
  templateUrl: './list-accounts.component.html',
  styleUrls: ['./list-accounts.component.scss']
})
export class ListAccountsComponent {
	
  displayedColumns = ['accountType', 'accountNumber','accountName'];
  accounts$ = this.accountService.getAccounts();
  
  constructor(private accountService:AccountService) { 
	accountService.fetch();
  }
  


  public selectRow(row:AccountData):void {
	  console.log(row);
  }
  
  public accountAdded(account:AccountData) {
	
  }

}

