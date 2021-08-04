package com.deboraeom.Soda.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SodaStockMissException extends Exception{

    public SodaStockMissException(Long id, int quantityInStock){
        super(String.format("Soda Stock with %s id doesn't have enough , quantity in stock is %s", id, quantityInStock));
    }



}
