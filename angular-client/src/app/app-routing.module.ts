import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListAccountsComponent } from './account/list-accounts/list-accounts.component';
import { AccountDetailsComponent } from './account/account-details/account-details.component';
import { CreateTransactionComponent } from './account/create-transaction/create-transaction.component';
import { CreateCompanyComponent } from './company/create-company/create-company.component';
import { ListCompanyComponent } from './company/list-company/list-company.component';
import { CreateInvoiceComponent } from './invoice/create-invoice/create-invoice.component';
import { ListInvoiceComponent } from './invoice/list-invoice/list-invoice.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { UpdateCompanyComponent } from './company/update-company/update-company.component';
import { InvoiceDetailComponent } from './invoice/invoice-detail/invoice-detail.component';
import { ErrorComponent } from './error/error.component';

const routes: Routes = [
	{path: '',  redirectTo: '/account/list', pathMatch: 'full' },
	{path:'account/list', component: ListAccountsComponent},
	{path:'account/transaction/create', component: CreateTransactionComponent},
	{path:'account/:id/transaction', component: AccountDetailsComponent},
	{path:'company/create', component: CreateCompanyComponent},
	{path:'company/list', component: ListCompanyComponent},
	{path:'company/update/:id', component: UpdateCompanyComponent},
	{path:'invoice/create', component: CreateInvoiceComponent},
	{path:'invoice/list', component: ListInvoiceComponent},
	{path:'invoice/:id/detail', component: InvoiceDetailComponent},
	{path:'error', component: ErrorComponent },
	{path:'**', component: NotFoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
