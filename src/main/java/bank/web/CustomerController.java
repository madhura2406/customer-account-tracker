package bank.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import bank.model.Customer;
import bank.repository.CustomerRepository;
import bank.service.AccountService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final AccountService accountService;
    private final CustomerRepository customerRepository;

    public CustomerController(AccountService accountService,
                              CustomerRepository customerRepository) {
        this.accountService = accountService;
        this.customerRepository = customerRepository;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,
                                                   @Valid @RequestBody Customer customer) {
        Customer updated = accountService.updateCustomer(id, customer);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return accountService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        return ResponseEntity.of(customerRepository.findById(id));
    }

    @GetMapping("/search")
    public List<Customer> searchByName(@RequestParam("name") String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }
}
