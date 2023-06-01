import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountService, AccountEntryData, AccountData, AccountDetails} from '../account.service';
import { ReplaySubject, Observable, map } from 'rxjs';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.scss']
})
export class AccountDetailsComponent {
	
	id:string;
	public readonly transactions$ = new ReplaySubject<AccountDetails>(1);
	
	displayedColumns = ['account', 'executedAt', 'description', 'debit', 'credit'];
	
	
	constructor(private activatedroute:ActivatedRoute,private accountService:AccountService) {
		this.id=this.activatedroute.snapshot.params["id"];
		this.fetchDetails();
	}
	
	public transactionAdded(data:AccountEntryData) {
		this.fetchDetails();
	}
	
	private fetchDetails() {
		this.accountService.getAccountDetails(this.id).subscribe(list=>this.transactions$.next(list));
	}
}
