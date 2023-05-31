import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject, Observable, catchError, throwError, map, tap, switchMap, retry, filter } from 'rxjs';

import { AppConfigService } from '../app-config.service'
import { HttpCrudServiceBase, FetchStatus } from '../common/http-crud-service.base';
import { HttpClient, HttpErrorResponse, HttpResponse } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  
	private readonly uri:string;
	protected readonly list$ = new BehaviorSubject<AccountData[]>([]);
    public readonly status$ = new BehaviorSubject<FetchStatus>(FetchStatus.INITIAL);
    protected readonly fetch$ = new Subject<string>();
	
	constructor(private http:HttpClient, private config:AppConfigService) { 
		this.uri =  config.getServerURL() + '/account';
		
		this.fetch$.pipe(
			switchMap(uri => this.http.get<AccountData[]>(uri)),
			retry()
		).subscribe(a=>this.fetchedOk(a), e=>this.fetchedError(e));
	}

	public fetch() {
		this.fetch$.next(this.uri);
	}

	public getAccountDetails(accountId:string): Observable<AccountDetails> {
		return this.http.get<AccountDetails>(this.uri + '/' + accountId ).pipe(
			catchError(this.handleError)
		);
	}

	private fetchedOk(a:AccountData[]) {
		this.list$.next(a); 
		this.status$.next(FetchStatus.OK);
	}

	private fetchedError(error: HttpErrorResponse) {
		this.status$.next(FetchStatus.FAILED);
	}
	
	protected handleError(error: HttpErrorResponse) {
		let code = "GeneralError";
		if (error.status === 404) {
			code = "NotFound";
		} 
		console.log(error);
		return throwError(() => code);
	}
	
	public getList(): Observable<AccountData[]> {
		return this.list$.asObservable();
	}
	
	public create(data:TransactionData) {
		return this.http.post<void>(this.uri + '/transaction', data).pipe(
			catchError(this.handleError),
			tap(() => this.fetch())
		);
	}

 
}

export interface AccountData {
	accountName: string;
}

export interface AccountEntryData {
	accountId: number;
	account:string;
	debit: number;
	credit: number;
	executedAt: Date;
	description: string;
}

export interface AccountDetails {
	account: string;
	transactions: AccountEntryData[];
	debit: number;
	credit: number;
}

export interface TransactionData {
	executedAt:string;
	entries: TransactionEntryData[]
}

export interface TransactionEntryData {
	account:string;
	debit:number;
	credit:number;
}