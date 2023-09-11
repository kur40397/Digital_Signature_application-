package com.BAN.Signature.Electronique.Exceptions.hundleException;

import com.BAN.Signature.Electronique.Exceptions.ErrorObject;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
@ControllerAdvice
public class ExceptionHandlerException {
    @ExceptionHandler(value = {MessagingException.class})
    public ResponseEntity<ErrorObject> handleMessagingException(MessagingException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
}
