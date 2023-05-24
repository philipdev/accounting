package untangle.accounting.server.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import untangle.accounting.data.AccountData;
import untangle.accounting.data.AccountDetails;
import untangle.accounting.data.AccountEntryData;
import untangle.accounting.server.service.AccountService;

@RestController
@RequestMapping("/api")
public class AccountController {
	
	AccountService accountService;
	
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@PostMapping("/account")
	public AccountData createAccount(@RequestBody AccountData accountData) {
		return this.accountService.createAccount(accountData);
	}
	
	@PostMapping("/account/transaction")
	public AccountEntryData createTransaction(@RequestBody AccountEntryData transactionData) {
		return this.accountService.creatEntry(transactionData);
	}
	
	@GetMapping("/account/{id}")
	public AccountData getAccount(@PathVariable("id") Long accountId) {
		return this.accountService.getAccount(accountId);
	}
	
	@GetMapping("/account/{id}/transaction")
	public AccountDetails getAccountDetails(@PathVariable("id") Long accountId) {
		return this.accountService.getAccountDetails(accountId);
	}

	@GetMapping("/account")
	public List<AccountData> getAccounts() {
		return this.accountService.getAccounts();
	}
}
