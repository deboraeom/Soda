package com.deboraeom.Soda.Controller;

import com.deboraeom.Soda.DTO.QuantityDTO;
import com.deboraeom.Soda.DTO.SodaDTO;
import com.deboraeom.Soda.Exception.SodaAlreadyIsRegisteredException;
import com.deboraeom.Soda.Exception.SodaNotFound;
import com.deboraeom.Soda.Exception.SodaStockExceededException;
import com.deboraeom.Soda.Exception.SodaStockMissException;
import com.deboraeom.Soda.Service.SodaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/soda")
public class sodaController {

    private final SodaService sodaService;

    public sodaController(SodaService sodaService) {
        this.sodaService = sodaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SodaDTO createSoda(@RequestBody @Valid SodaDTO sodaDTO) throws SodaAlreadyIsRegisteredException {
        return sodaService.createSoda(sodaDTO);
    }

    @GetMapping
    public List<SodaDTO> listSoda(){
        return sodaService.findAllSodas();
    }

    @GetMapping("/{name}")
    public SodaDTO findByName(@PathVariable String name) throws SodaNotFound{
        return sodaService.findByName(name);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) throws SodaNotFound{
        sodaService.deleteSoda(id);
    }

    @PatchMapping("/{id}/increment")
    public SodaDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO)
            throws SodaNotFound, SodaStockExceededException{
        return sodaService.sodaIncrement(id,quantityDTO.getQuantity());

    }

    @PatchMapping("/{id}/decrement")
    public SodaDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO)
            throws SodaNotFound, SodaStockMissException {
        return sodaService.sodadecrement(id,quantityDTO.getQuantity());

    }
}
