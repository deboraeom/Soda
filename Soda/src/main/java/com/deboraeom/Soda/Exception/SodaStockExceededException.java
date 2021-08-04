package com.deboraeom.Soda.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SodaStockExceededException extends Exception{

    public SodaStockExceededException(Long id, int quantityToIncrement){
        super(String.format("Soda Stock with %s id exceeded, max stock capacity to add is %s", id, quantityToIncrement));
    }



}
