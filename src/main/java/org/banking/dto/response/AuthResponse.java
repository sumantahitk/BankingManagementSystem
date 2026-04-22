package org.banking.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private java.util.UUID customerId;
    private String username;
    private String email;
    private String fullName;
    private String role;
}
