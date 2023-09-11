package com.BAN.Signature.Electronique.Exceptions.hundleException;

import com.BAN.Signature.Electronique.Exceptions.ErrorObject;
import com.BAN.Signature.Electronique.Exceptions.Exception.DocFileAlreadySignedException;
import com.BAN.Signature.Electronique.Exceptions.Exception.DocfileNotFoundException;
import com.BAN.Signature.Electronique.Exceptions.Exception.NonePdfFoundException;
import com.BAN.Signature.Electronique.Exceptions.Exception.NoneSignedPdfFoundException;
import com.qoppa.pdf.PDFException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandlerDocfile {
    @ExceptionHandler(value = {DocfileNotFoundException.class})
    public ResponseEntity<ErrorObject> handleDocFileNotFoundException(DocfileNotFoundException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {PDFException.class})
    public ResponseEntity<ErrorObject> handlePDFException(PDFException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {NoneSignedPdfFoundException.class})
    public ResponseEntity<ErrorObject> handleNoneSignedPdfFoundException(NoneSignedPdfFoundException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {DocFileAlreadySignedException.class})
    public ResponseEntity<ErrorObject> handleDocFileAlreadySignedException(DocFileAlreadySignedException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {NonePdfFoundException.class})
    public ResponseEntity<ErrorObject> handleNonePdfFoundException(NonePdfFoundException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }

}
