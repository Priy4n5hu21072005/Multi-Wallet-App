package com.example.multiwallet.exception;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String msg){
        super(msg);
    }
}
