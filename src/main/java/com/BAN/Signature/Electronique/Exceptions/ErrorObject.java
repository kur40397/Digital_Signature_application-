package com.BAN.Signature.Electronique.Exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
@Data
public class ErrorObject {
    private final String message;

    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
