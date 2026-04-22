package org.banking.dto.response;

import lombok.*;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CustomerResponse {
    private UUID id;
    private String customerName;
    private String email;
    private String phone;
    private String address;
}
