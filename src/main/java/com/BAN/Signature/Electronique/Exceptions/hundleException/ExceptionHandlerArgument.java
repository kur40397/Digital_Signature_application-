package com.BAN.Signature.Electronique.Exceptions.hundleException;

import com.BAN.Signature.Electronique.Exceptions.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
@ControllerAdvice
public class ExceptionHandlerArgument {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorObject> HandlerMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
}
