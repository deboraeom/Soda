package com.deboraeom.Soda.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SodaNotFound extends Exception{
    public SodaNotFound(String sodaName) {
        super(String.format("Soda with name %s not found.", sodaName));
    }

    public SodaNotFound(Long sodaId) {
        super(String.format("Soda with id %s not found.", sodaId));
    }

}
