import { Component } from '@angular/core';
import { CompanyData, CompanyService} from './company/company.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'angular-client';
  ownerCompany$ = this.companyService.getOwnerCompany();
  
  constructor(private companyService:CompanyService) {
  }
  
  public get hasOwner() {
	return this.companyService.hasOwner;
  }
}
