package com.BAN.Signature.Electronique.Exceptions.hundleException;

import com.BAN.Signature.Electronique.Exceptions.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandlerSecurity {
    @ExceptionHandler(value = {GeneralSecurityException.class})
    public ResponseEntity<ErrorObject> handleGeneralSecurityException(GeneralSecurityException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
}
