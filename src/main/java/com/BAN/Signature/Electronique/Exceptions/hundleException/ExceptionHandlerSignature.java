package com.BAN.Signature.Electronique.Exceptions.hundleException;

import com.BAN.Signature.Electronique.Exceptions.ErrorObject;
import com.BAN.Signature.Electronique.Exceptions.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandlerSignature {
    @ExceptionHandler(value = {SignatureNotFoundException.class})
    public ResponseEntity<ErrorObject> handleSignatureNotFoundException(SignatureNotFoundException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {NoneSignatureFoundException.class})
    public ResponseEntity<ErrorObject> handleNoneSignatureFoundException(NoneSignatureFoundException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {SignatureAlreadyExiste.class})

    public ResponseEntity<ErrorObject> handleSignatureAlreadyExiste(SignatureAlreadyExiste ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {SignatureAlreadyUsedException.class})
    public ResponseEntity<ErrorObject> handleSignatureAlreadyUsedException(SignatureAlreadyUsedException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {ImageDimensionException.class})
    public ResponseEntity<ErrorObject> handleImageDimensionException(ImageDimensionException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {NoneSignatureExistInAPdfDocException.class})
    public ResponseEntity<ErrorObject> handleNoneSignatureExistInAPdfDocException(NoneSignatureExistInAPdfDocException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
}
