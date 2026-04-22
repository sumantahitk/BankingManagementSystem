package org.banking.dto.response;

import lombok.*;
import org.banking.entity.TransactionStatus;
import org.banking.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private TransactionStatus status;
    private String description;
    private String referenceNumber;
    private Long accountNumber;
    private java.util.UUID customerId;
    private String senderName;
    private String receiverName;
}
