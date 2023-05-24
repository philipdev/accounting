import { LOCALE_ID, APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule} from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule, MatIconRegistry} from '@angular/material/icon';
import { MatListModule} from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgxMatDatetimePickerModule, NgxMatTimepickerModule,NgxMatDateFormats,NGX_MAT_DATE_FORMATS} from '@angular-material-components/datetime-picker';
import { NgxMatMomentModule } from '@angular-material-components/moment-adapter';
import { MatMomentDateModule, MomentDateAdapter } from "@angular/material-moment-adapter";

import { MatFormFieldModule} from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatExpansionModule } from '@angular/material/expansion';
import { ListAccountsComponent } from './account/list-accounts/list-accounts.component';
import { CreateAccountComponent } from './account/create-account/create-account.component';
import { AccountDetailsComponent } from './account/account-details/account-details.component';

import {MatDatepickerModule} from '@angular/material/datepicker';
import { registerLocaleData } from '@angular/common';
import localeNL from '@angular/common/locales/nl';

import { CreateCompanyComponent } from './company/create-company/create-company.component';
import { ListCompanyComponent } from './company/list-company/list-company.component';
import { CompanyFormComponent } from './company/company-form/company-form.component';
import { CreateInvoiceComponent } from './invoice/create-invoice/create-invoice.component';
import { ListInvoiceComponent } from './invoice/list-invoice/list-invoice.component';
import { AppConfigService } from './app-config.service';
import { NotFoundComponent } from './not-found/not-found.component';
import { UpdateCompanyComponent } from './company/update-company/update-company.component';
import { InvoiceDetailComponent } from './invoice/invoice-detail/invoice-detail.component';
import { ErrorComponent } from './error/error.component';

registerLocaleData(localeNL);


const CUSTOM_DATE_FORMATS: NgxMatDateFormats = {
  parse: {
    dateInput: 'l, LTS'
  },
  display: {
    dateInput: 'DD/MM/YYYY HH:mm',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  }
};

const DateFormats = {
	parse: {
		dateInput: ['YYYY-MM-DD']
	},
	display: {
		dateInput: 'YYYY-MM-DD',
		monthYearLabel: 'MMM YYYY',
		dateA11yLabel: 'LL',
		monthYearA11yLabel: 'MMMM YYYY',
	},
};

const initializerConfigFn = (appConfig: AppConfigService) => {
  return () => {
    return appConfig.loadAppConfig();
  };
};

@NgModule({
  declarations: [
    AppComponent,
    CreateAccountComponent,
    ListAccountsComponent,
    AccountDetailsComponent,
    CreateCompanyComponent,
    ListCompanyComponent,
    CompanyFormComponent,
    CreateInvoiceComponent,
    ListInvoiceComponent,
    NotFoundComponent,
    UpdateCompanyComponent,
    InvoiceDetailComponent,
    ErrorComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
	ReactiveFormsModule,
    HttpClientModule,
	HttpClientXsrfModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
	MatFormFieldModule,
	MatSelectModule,
	MatInputModule,
	MatTableModule,
	MatExpansionModule,
    NgxMatTimepickerModule,
	NgxMatDatetimePickerModule,
	NgxMatMomentModule,
	MatDatepickerModule,
	MatCheckboxModule,
	MatMomentDateModule,
	AppRoutingModule,
	
  ],
  providers: [	{provide: NGX_MAT_DATE_FORMATS, useValue: CUSTOM_DATE_FORMATS },
				{provide: LOCALE_ID, useValue: 'nl-BE' },
				{
				  provide: APP_INITIALIZER,
				  useFactory: initializerConfigFn,
				  multi: true,
				  deps: [AppConfigService],
				}],
  
  bootstrap: [AppComponent]
})
export class AppModule { }



