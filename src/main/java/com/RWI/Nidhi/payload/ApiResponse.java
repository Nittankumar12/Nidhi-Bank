package com.RWI.Nidhi.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApiResponse {
    private String message;
    private boolean success;
    private HttpStatus status;
}
