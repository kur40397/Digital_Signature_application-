package com.BAN.Signature.Electronique.Exceptions.hundleException;

import com.BAN.Signature.Electronique.Exceptions.ErrorObject;
import com.BAN.Signature.Electronique.Exceptions.Exception.EmployeeNotFoundException;
import com.BAN.Signature.Electronique.Exceptions.Exception.NoneEmployeeFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
@ControllerAdvice
public class ExceptionHandlerEmployee {
    @ExceptionHandler(value = {EmployeeNotFoundException.class})

    //kat mappi entre l'exception & la method

    // hna fin kathandli wa7d l'exception specifique


    public ResponseEntity<ErrorObject> handleEmployeeNotFoundException(EmployeeNotFoundException ex){
        ErrorObject errorObject= new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {NoneEmployeeFoundException.class})

    public ResponseEntity<ErrorObject> handleNoneEmployeeFoundException(NoneEmployeeFoundException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
}
