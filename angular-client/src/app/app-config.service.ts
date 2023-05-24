import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AppConfigService {

	private appConfig?:AppConfig;

	constructor(private http: HttpClient) {}

	loadAppConfig() {
		return this.http
		  .get('/assets/config.json')
		  .toPromise()
		  .then(data => {
			this.appConfig = data as AppConfig;
		});
	}
	
	getBaseUrl(): string {
		let serverUrl ="";
		const config = this.appConfig;
		if(config && !config.USE_ORIGIN) {
			serverUrl =config.API_URL;
		}
		return serverUrl;
	}
	getServerURL(): string {
		const url = this.getBaseUrl();
		return url + '/api';
	}
}

interface AppConfig {
	USE_ORIGIN:boolean;
	API_URL:string;
}