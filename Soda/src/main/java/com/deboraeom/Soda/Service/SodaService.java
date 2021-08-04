package com.deboraeom.Soda.Service;


import com.deboraeom.Soda.DTO.SodaDTO;
import com.deboraeom.Soda.Entity.Soda;
import com.deboraeom.Soda.Exception.SodaAlreadyIsRegisteredException;
import com.deboraeom.Soda.Exception.SodaNotFound;
import com.deboraeom.Soda.Exception.SodaStockExceededException;
import com.deboraeom.Soda.Exception.SodaStockMissException;
import com.deboraeom.Soda.Mapper.SodaMapper;
import com.deboraeom.Soda.Repository.SodaRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@org.springframework.stereotype.Service
@AllArgsConstructor
public class SodaService {
    private final SodaRepository sodaRepository;
    private final SodaMapper sodaMapper = SodaMapper.INSTANCE;

    public Soda verifyIfExistById(Long id) throws SodaNotFound {
        return sodaRepository.findById(id)
                .orElseThrow(()-> new SodaNotFound(id));
            }

    public void verifyIfExistByName(String name)throws SodaAlreadyIsRegisteredException{
        Optional<Soda> optSodaSaved = sodaRepository.findByName(name);
        if(optSodaSaved.isPresent()){
            throw new SodaAlreadyIsRegisteredException(name);
        }
    }
    public List<SodaDTO> findAllSodas(){
       return sodaRepository.findAll()
                .stream()
                .map(sodaMapper::toDTO)
                .collect(Collectors.toList());

    }

    public SodaDTO findByName(String name) throws SodaNotFound{
        Soda soda = sodaRepository.findByName(name)
                .orElseThrow(()-> new SodaNotFound(name));
        return sodaMapper.toDTO(soda);


    }

    public void deleteSoda(Long id) throws SodaNotFound{

        sodaRepository.delete(verifyIfExistById(id));
    }

    public SodaDTO createSoda(SodaDTO sodaDTO) throws SodaAlreadyIsRegisteredException {
        verifyIfExistByName(sodaDTO.getName());
        Soda soda = sodaMapper.toModel(sodaDTO);
        Soda sodaSaved = sodaRepository.save(soda);
        return sodaMapper.toDTO(sodaSaved);
    }

    public SodaDTO sodaIncrement(Long id, int quantityToIncrement)
            throws SodaNotFound, SodaStockExceededException{

        Soda sodaToIncrement = verifyIfExistById(id);
        int quantityAfterIncrement = sodaToIncrement.getQuantity()+quantityToIncrement;
        if(quantityAfterIncrement<= sodaToIncrement.getMax()){
            sodaToIncrement.setQuantity(quantityAfterIncrement);
            return sodaMapper.toDTO(sodaRepository.save(sodaToIncrement));

        }
        throw new SodaStockExceededException(id, sodaToIncrement.getMax()-sodaToIncrement.getQuantity());

    }

    public SodaDTO sodadecrement(Long id, int quantityToDecrement)
            throws SodaNotFound,  SodaStockMissException {

        Soda sodaToDecrement = verifyIfExistById(id);
        int quantityAfterDecrement = sodaToDecrement.getQuantity()-quantityToDecrement;
        if(quantityAfterDecrement>= 0){
            sodaToDecrement.setQuantity(quantityAfterDecrement);
            return sodaMapper.toDTO(sodaRepository.save(sodaToDecrement));

        }
        throw new SodaStockMissException(id, sodaToDecrement.getQuantity());

    }



}
