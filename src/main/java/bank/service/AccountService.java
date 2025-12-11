package bank.service;

import bank.model.Account;
import bank.model.Customer;

import java.util.List;

public interface AccountService {

    String addAccount(Account account);

    List<Account> getAllAccounts();

    List<Customer> getAllCustomers();

    String transferFunds(int from, int to, double amount);

    Account getBalanceOf(int accountNumber);

    Customer updateCustomer(Long customerId, Customer updatedCustomer);

    Customer getCustomerById(Long customerId);
}
