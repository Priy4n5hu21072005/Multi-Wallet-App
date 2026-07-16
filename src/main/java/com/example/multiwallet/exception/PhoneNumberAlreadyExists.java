package com.example.multiwallet.exception;

public class PhoneNumberAlreadyExists extends RuntimeException{
    public PhoneNumberAlreadyExists(String msg){
        super(msg);
    }
}
