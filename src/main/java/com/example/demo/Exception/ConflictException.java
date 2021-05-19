package com.example.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//annotation : when the exception occur, response the HTTP status to the font end
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException{
    public ConflictException() {
        super();
    }

    public ConflictException(String message) {
        super(message);
    }
}
