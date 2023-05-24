import { Injectable } from '@angular/core';
import { Observable, catchError} from 'rxjs';
import { HttpClient } from "@angular/common/http";
import { AppConfigService } from '../app-config.service'
import { HttpCrudServiceBase, FetchStatus } from '../common/http-crud-service.base';

@Injectable({
  providedIn: 'root'
})
export class AccountService extends HttpCrudServiceBase<AccountData> {
   
  constructor(http:HttpClient, private config:AppConfigService) { 
	super(config.getServerURL() + '/account', http);
	this.fetch();
  }
  
  public createAccount(data:AccountData):Observable<AccountData> {
	return this.create(data);
  }
  
  public getAccounts(): Observable<AccountData[]> {
	return this.getList();
  }
	
  public getAccountDetails(accountId:any): Observable<AccountDetails> {
	return this.http.get<AccountDetails>(this.uri + '/' + accountId + '/transaction').pipe(
      catchError(this.handleError)
	);
  }
 
}

export interface AccountData {
  id?:number;
  accountType: string;
  accountName: string;
  accountNumber: string;
}

export interface TransactionData {
	id?:number;
	accountId: number;
	debit: number;
	credit: number;
	executedAt: Date;
	description: string;
}

export interface AccountDetails {
	account: AccountData;
	transactions: TransactionData[];
	debit: number;
	credit: number;
}