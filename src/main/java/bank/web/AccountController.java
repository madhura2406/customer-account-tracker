package bank.web;

import bank.web.dto.CreateAccountRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import bank.model.Account;
import bank.model.Customer;
import bank.service.AccountService;
import bank.web.dto.TransferRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<String> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Customer customer = Customer.builder()
                .name(request.getCustomerName())
                .email(request.getCustomerEmail())
                .phone(request.getCustomerPhone())
                .address(request.getCustomerAddress())
                .build();

        Account account = Account.builder()
                .accountNumber(request.getAccountNumber())
                .accountType(request.getAccountType())
                .balance(request.getInitialBalance())
                .customer(customer)
                .build();

        String status = accountService.addAccount(account);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest request) {
        String result = accountService.transferFunds(
                request.getFromAccountNumber(),
                request.getToAccountNumber(),
                request.getAmount()
        );
        HttpStatus status = "SUCCESS".equals(result) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(result, status);
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<Account> getBalance(@PathVariable int accountNumber) {
        Account account = accountService.getBalanceOf(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }
}
