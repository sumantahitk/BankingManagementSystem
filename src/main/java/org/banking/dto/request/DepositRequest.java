package org.banking.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class DepositRequest {
    @NotNull @DecimalMin("0.01") private BigDecimal amount;
    private String description;
}
