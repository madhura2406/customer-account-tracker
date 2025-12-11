package bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import bank.model.Account;

import bank.repository.AccountRepository;
import bank.repository.CustomerRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void testTransferFundsSuccess() {
        Account from = Account.builder().accountNumber(1).balance(1000.0).build();
        Account to = Account.builder().accountNumber(2).balance(500.0).build();

        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(from));
        when(accountRepository.findByAccountNumber(2)).thenReturn(Optional.of(to));

        String result = accountService.transferFunds(1, 2, 200.0);

        assertThat(result).isEqualTo("SUCCESS");
        assertThat(from.getBalance()).isEqualTo(800.0);
        assertThat(to.getBalance()).isEqualTo(700.0);

        verify(accountRepository, times(1)).save(from);
        verify(accountRepository, times(1)).save(to);
    }

    @Test
    void testTransferFundsInsufficientFunds() {
        Account from = Account.builder().accountNumber(1).balance(100.0).build();
        Account to = Account.builder().accountNumber(2).balance(500.0).build();

        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(from));
        when(accountRepository.findByAccountNumber(2)).thenReturn(Optional.of(to));

        String result = accountService.transferFunds(1, 2, 200.0);

        assertThat(result).isEqualTo("INSUFFICIENT FUNDS");
        verify(accountRepository, never()).save(from);
        verify(accountRepository, never()).save(to);
    }

    @Test
    void testTransferFundsIdMismatch() {
        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.empty());
        when(accountRepository.findByAccountNumber(2)).thenReturn(Optional.empty());

        String result = accountService.transferFunds(1, 2, 50.0);

        assertThat(result).isEqualTo("ID MISMATCH");
    }
}
