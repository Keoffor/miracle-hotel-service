package com.miracle.keoffor.exception;

public class InvalidCodeBookingRequestException extends RuntimeException{
    public InvalidCodeBookingRequestException(String message){
        super(message);
    }
}
