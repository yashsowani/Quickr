package com.example.Quickr.CustomExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="URL Not Found")
public class URLNotFoundException extends Exception{
    private static final long serialVersionUID = -3332292346834265371L;

    public URLNotFoundException(String message){
        super("URLNotFoundException with message="+message);
    }
}
