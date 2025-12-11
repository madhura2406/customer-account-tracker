package bank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bank.model.Account;
import bank.model.Customer;
import bank.repository.AccountRepository;
import bank.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public String addAccount(Account account) {
        if (account.getCustomer() == null) {
            throw new IllegalArgumentException("Customer details are required");
        }

        Customer customer = account.getCustomer();

        // Save or attach existing customer
        if (customer.getId() != null && customerRepository.existsById(customer.getId())) {
            customer = customerRepository.findById(customer.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        } else {
            customer = customerRepository.save(customer);
        }

        // Ensure one account per customer
        if (accountRepository.existsByCustomerId(customer.getId())) {
            throw new IllegalStateException("Customer already has an account");
        }

        account.setCustomer(customer);
        accountRepository.save(account);

        // link back reference
        customer.setAccount(account);
        customerRepository.save(customer);

        return "SUCCESS";
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAllWithAccounts();
    }

    @Override
    public String transferFunds(int from, int to, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }

        Optional<Account> fromOpt = accountRepository.findByAccountNumber(from);
        Optional<Account> toOpt = accountRepository.findByAccountNumber(to);

        if (!fromOpt.isPresent() || !toOpt.isPresent()) {
            return "ID MISMATCH";
        }

        Account fromAcc = fromOpt.get();
        Account toAcc = toOpt.get();

        if (fromAcc.getBalance() < amount) {
            return "INSUFFICIENT FUNDS";
        }

        fromAcc.setBalance(fromAcc.getBalance() - amount);
        toAcc.setBalance(toAcc.getBalance() + amount);

        accountRepository.save(fromAcc);
        accountRepository.save(toAcc);

        return "SUCCESS";
    }

    @Override
    @Transactional(readOnly = true)
    public Account getBalanceOf(int accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElse(null);
    }

    @Override
    public Customer updateCustomer(Long customerId, Customer updatedCustomer) {
        Customer existing = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        existing.setName(updatedCustomer.getName());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setPhone(updatedCustomer.getPhone());
        existing.setAddress(updatedCustomer.getAddress());

        return customerRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
    }
}
