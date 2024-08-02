package com.supplyhouse.assignment.model.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * This is a generic error response data holder
 * */
@Getter
@Setter
public class ErrorDTO {
    private HttpStatus status;
    private String errorMessage;
    private String stacktrace;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorDTO(HttpStatus status, String errorMessage, String stacktrace) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.stacktrace = stacktrace;
    }
}
