package com.sprintlog.sprintlogboot.excepion;

public class InvalidActivityException extends RuntimeException {

    //ctor
    public InvalidActivityException(String message) {
        super(message);
    }
}
