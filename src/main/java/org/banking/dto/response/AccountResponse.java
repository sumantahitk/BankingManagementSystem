package org.banking.dto.response;

import lombok.*;
import org.banking.entity.AccountStatus;
import org.banking.entity.AccountType;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AccountResponse {
    private Long accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus status;
}
