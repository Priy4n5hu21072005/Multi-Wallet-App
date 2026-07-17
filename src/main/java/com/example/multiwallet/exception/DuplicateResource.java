package com.example.multiwallet.exception;

public class DuplicateResource extends RuntimeException{
    public DuplicateResource(String msg){
        super(msg);
    }
}
