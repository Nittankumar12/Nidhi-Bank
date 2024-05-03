package com.RWI.Nidhi.exception;

import com.RWI.Nidhi.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAccountNotFoundException(AccountNotFoundException accountNotFoundException) {
        String message = accountNotFoundException.getMessage();
        ApiResponse response = ApiResponse.builder().message(message)
                .success(true).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountIdNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAccountIdNotFoundException(AccountIdNotFoundException accountIdNotFoundException) {
        String message = accountIdNotFoundException.getMessage();
        ApiResponse response = ApiResponse.builder().message(message)
                .success(true).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OtpNotSendException.class)
    public ResponseEntity<ApiResponse> handleOtpNotSendHandler(OtpNotSendException otpNotSendException) {
        String message = otpNotSendException.getMessage();
        ApiResponse response = ApiResponse.builder().message(message)
                .success(true).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OTPExpireException.class)
    public ResponseEntity<ApiResponse> handleOTPExpireException(OTPExpireException otpExpireException) {
        String message = otpExpireException.getMessage();
        ApiResponse response = ApiResponse.builder().message(message)
                .success(true).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}
