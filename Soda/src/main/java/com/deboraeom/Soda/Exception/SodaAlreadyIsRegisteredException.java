package com.deboraeom.Soda.Exception;

import com.deboraeom.Soda.Entity.Soda;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SodaAlreadyIsRegisteredException extends Exception{

    public SodaAlreadyIsRegisteredException(String sodaName){
        super(String.format("Soda with name %s already registered in the system.", sodaName));
    }
}
