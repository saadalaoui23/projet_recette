// com.example.user_service.dtos/AuthResponse.java

package com.example.user_service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    public AuthResponse(String token) {
        this.token = token;

    }
}