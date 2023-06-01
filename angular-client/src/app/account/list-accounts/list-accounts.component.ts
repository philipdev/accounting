import { Component, inject } from '@angular/core';
import { AccountService, AccountData} from '../account.service';
import { FormBuilder, Validators, FormArray} from '@angular/forms';

@Component({
  selector: 'app-list-accounts',
  templateUrl: './list-accounts.component.html',
  styleUrls: ['./list-accounts.component.scss']
})
export class ListAccountsComponent {
	
	private accountService = inject(AccountService);
	protected accounts$ = this.accountService.getList();
	
	displayedColumns = ['account'];
	
	constructor() {
		this.accountService.fetch();
	}
}

