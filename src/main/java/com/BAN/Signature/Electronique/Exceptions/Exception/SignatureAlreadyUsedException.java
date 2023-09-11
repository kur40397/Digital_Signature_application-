package com.BAN.Signature.Electronique.Exceptions.Exception;

public class SignatureAlreadyUsedException extends RuntimeException{
    public SignatureAlreadyUsedException(String message) {
        super(message);
    }
}
