package com.BAN.Signature.Electronique.Exceptions.Exception;

public class DocFileAlreadySignedException extends RuntimeException{
    public DocFileAlreadySignedException(String message) {
        super(message);
    }
}
