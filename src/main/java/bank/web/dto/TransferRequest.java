package bank.web.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class TransferRequest {

    @NotNull
    private Integer fromAccountNumber;

    @NotNull
    private Integer toAccountNumber;

    @NotNull
    @Min(1)
    private Double amount;
}
