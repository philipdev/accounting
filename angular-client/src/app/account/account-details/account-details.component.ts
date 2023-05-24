import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountService, TransactionData, AccountData, AccountDetails} from '../account.service';
import { ReplaySubject, Observable, map } from 'rxjs';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.scss']
})
export class AccountDetailsComponent {
	
	id:number;
	public readonly transactions$ = new ReplaySubject<AccountDetails>(1);
	
	displayedColumns = ['executedAt','description','debit', 'credit'];
	
	
	constructor(private activatedroute:ActivatedRoute,private accountService:AccountService) {
		this.id=parseInt(this.activatedroute.snapshot.params["id"]);
		this.fetchDetails();
	}
	
	public transactionAdded(data:TransactionData) {
		this.fetchDetails();
	}
	
	private fetchDetails() {
		this.accountService.getAccountDetails(this.id).subscribe(list=>this.transactions$.next(list));
	}
}
