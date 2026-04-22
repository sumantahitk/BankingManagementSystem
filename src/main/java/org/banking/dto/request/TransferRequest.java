package org.banking.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class TransferRequest {
    @NotNull private Long toAccountNumber;
    @NotNull @DecimalMin("0.01") private BigDecimal amount;
    @jakarta.validation.constraints.NotBlank 
    @jakarta.validation.constraints.Size(min = 4, max = 4) 
    private String pin;
    private String description;
}
