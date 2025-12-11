package bank.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import bank.model.Account;
import bank.model.AccountType;
import bank.model.Customer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveAndFindByAccountNumber() {
        Customer customer = Customer.builder()
                .name("John Doe")
                .email("john@example.com")
                .build();
        customer = customerRepository.save(customer);

        Account account = Account.builder()
                .accountNumber(1001)
                .accountType(AccountType.SAVINGS_INDIVIDUAL)
                .balance(1000.0)
                .customer(customer)
                .build();

        account = accountRepository.save(account);

        List<Account> all = accountRepository.findAll();
        assertThat(all).hasSize(1);

        Optional<Account> byAccNo = accountRepository.findByAccountNumber(1001);
        assertThat(byAccNo).isPresent();
        assertThat(byAccNo.get().getBalance()).isEqualTo(1000.0);
    }

    @Test
    void testDeleteAndFindAll() {
        Customer customer = Customer.builder().name("Jane").build();
        customer = customerRepository.save(customer);

        Account account = Account.builder()
                .accountNumber(2001)
                .accountType(AccountType.CURRENT)
                .balance(500.0)
                .customer(customer)
                .build();
        account = accountRepository.save(account);

        accountRepository.deleteById(account.getId());
        assertThat(accountRepository.findAll()).isEmpty();
    }
}
