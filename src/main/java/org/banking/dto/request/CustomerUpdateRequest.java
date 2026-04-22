package org.banking.dto.request;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CustomerUpdateRequest {
    private String address;
    private LocalDate dob;
}
