package org.banking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.banking.entity.AccountType;
import java.math.BigDecimal;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AccountCreateRequest {
    @NotNull private UUID customerId;
    @NotNull private AccountType accountType;
    @NotNull private BigDecimal initialBalance;
    @jakarta.validation.constraints.NotBlank 
    @jakarta.validation.constraints.Size(min = 4, max = 4) 
    private String pin;
}
