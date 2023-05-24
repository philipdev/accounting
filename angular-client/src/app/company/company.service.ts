import { Injectable } from '@angular/core';
import { Observable, map, filter, of, catchError, debounceTime, distinct  } from 'rxjs';
import { HttpClient, HttpParams } from "@angular/common/http";
import { AppConfigService } from '../app-config.service';
import { HttpCrudServiceBase, FetchStatus } from '../common/http-crud-service.base';
import {  AsyncValidatorFn,  AbstractControl, ValidationErrors} from '@angular/forms';


@Injectable({
  providedIn: 'root'
})
export class CompanyService extends HttpCrudServiceBase<CompanyData> {
  
  constructor(http:HttpClient, private config:AppConfigService) { 
	super(config.getServerURL() + '/company', http);
  }
  
  public createCompany(data:CompanyData):Observable<CompanyData> {
	return this.create(data);
  } 
  
  public getCompanies(): Observable<CompanyData[]> {
	return this.getList();
  }
  
  public getCompany(id:number): Observable<CompanyData> {
	return this.get(id);
  }
  
  public getOwnerCompany() : Observable<CompanyData>{
	return this.getList().pipe(
		map(
			comp => comp.find(e=> e.owner)
		),
		filter(comp=> comp !== undefined),
	) as Observable<CompanyData>;
  }
  
  public get hasOwner() 	{
	let companies = this.list$.getValue();
	if(!companies) {
		companies = [];
	}
	return companies.find(e=> e.owner) != undefined;
  }
  
  public checkNewName(name:string): Observable<boolean> {
		console.log('checkNewName', name);
		return this.http.get<void>(this.uri + '/validate/name/' + name).pipe (
			map( ok => true)
		);
  }

  
  public vatValidator(id: number | null):AsyncValidatorFn  {
	let params = new HttpParams();
	if(id !== null) {
		params = params.append("current", id);
	}
    return (control: AbstractControl) => {
        return this.http.get<void>(this.uri + '/validate/vat/' + control.value, {params:params}).pipe (
			distinct(),
			debounceTime(200),
			map( ok => null),
			catchError(e=> of({vatExists:true}))
		);
    }
  }
  
  public nameValidator(id: number | null):AsyncValidatorFn  {
	let params = new HttpParams();
	if(id !== null) {
		params = params.append("current", id);
	}
    return (control: AbstractControl) => {
        return this.http.get<void>(this.uri + '/validate/name/' + control.value, {params:params}).pipe (
			distinct(),
			debounceTime(200),
			map( ok => null),
			catchError(e=> of({nameExists:true}))
		);
    }
  }
}



export interface CompanyData {
  id?:number;
  name: string;
  vat: string;
  address: string;
  zipCode: string;
  city: string;
  country: string;
  contact: string;
  email: string;
  phoneNumber: string;
  owner: boolean;
  ownerData: OwnerData;
}

export interface OwnerData {
  iban: string;
  bic: string;
  jurisdiction: string;
}
