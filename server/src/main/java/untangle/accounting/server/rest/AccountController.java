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
import untangle.accounting.data.TransactionData;
import untangle.accounting.server.service.AccountService;

@RestController
@RequestMapping("/api")
public class AccountController {
	
	AccountService accountService;
	
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	
	@GetMapping("/account/{name}")
	public AccountDetails getAccountDetails(@PathVariable("name") String account) {
		return this.accountService.getAccountDetails(account);
	}

	@GetMapping("/account")
	public List<AccountData> getAccounts() {
		return this.accountService.getAccounts();
	}
	
	@PostMapping("/account/transaction")
	public void createTransaction(@RequestBody TransactionData trxData) {
		this.accountService.createTransaction(trxData);
	}
}
