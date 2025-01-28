package com.grok.exceptions;

import java.io.ObjectInputFilter.Status;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    private LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus status;
    private String error;
    private String detail;
}
