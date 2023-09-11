package com.BAN.Signature.Electronique.Exceptions.hundleException;

import com.BAN.Signature.Electronique.Exceptions.ErrorObject;
import com.BAN.Signature.Electronique.Exceptions.Exception.ImageDimensionException;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandlerFile {
    @ExceptionHandler(value = {InvalidFileNameException.class})
    public ResponseEntity<ErrorObject> handleInvalidFileNameException(InvalidFileNameException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {InvalidContentTypeException.class})
    public ResponseEntity<ErrorObject> handleInvalidContentTypeException(InvalidContentTypeException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public ResponseEntity<ErrorObject> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {ImageDimensionException.class})
    public ResponseEntity<ErrorObject> handleImageDimensionException(ImageDimensionException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {FileNotFoundException.class})
    public ResponseEntity<ErrorObject> handleFileNotFoundException(FileNotFoundException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<ErrorObject> handleIOException(IOException ex){
        ErrorObject errorObject=new ErrorObject(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorObject,errorObject.getHttpStatus());
    }
}
