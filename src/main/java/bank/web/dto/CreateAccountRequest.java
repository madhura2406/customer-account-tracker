package bank.web.dto;

import lombok.Data;
import bank.model.AccountType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateAccountRequest {

    @NotNull
    private Integer accountNumber;

    @NotNull
    private AccountType accountType;

    @NotNull
    private Double initialBalance;

    @NotNull
    @Size(min = 1, max = 100)
    private String customerName;

    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
}
