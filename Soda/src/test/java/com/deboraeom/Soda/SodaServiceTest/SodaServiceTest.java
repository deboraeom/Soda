package com.deboraeom.Soda.SodaServiceTest;

import com.deboraeom.Soda.DTO.SodaDTO;
import com.deboraeom.Soda.Entity.Soda;
import com.deboraeom.Soda.Exception.SodaAlreadyIsRegisteredException;
import com.deboraeom.Soda.Exception.SodaNotFound;
import com.deboraeom.Soda.Exception.SodaStockExceededException;
import com.deboraeom.Soda.Exception.SodaStockMissException;
import com.deboraeom.Soda.Mapper.SodaMapper;
import com.deboraeom.Soda.Repository.SodaRepository;
import com.deboraeom.Soda.Service.SodaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SodaServiceTest {

    private static final long INVALID_SODA_ID =1L;


    @Mock
    private SodaRepository sodaRepository;

    private SodaMapper sodaMapper= SodaMapper.INSTANCE;

    @InjectMocks
    private SodaService sodaService;



    @Test
    void whenSodaInformedThenItShouldBeCreated() throws SodaAlreadyIsRegisteredException{

         SodaDTO expectedSodaDto = SodaDTO.builder().build();
         Soda expectedSavedSoda = sodaMapper.toModel(expectedSodaDto);

         when(sodaRepository.findByName((expectedSodaDto.getName()))).thenReturn(Optional.empty());
         when(sodaRepository.save(expectedSavedSoda)).thenReturn(expectedSavedSoda);

         SodaDTO createdSodaDTO = sodaService.createSoda(expectedSodaDto);
//         assertEquals(expectedSavedSoda.getId(), createdSodaDTO.getId());
//         assertEquals(expectedSavedSoda.getName(), createdSodaDTO.getName());

        assertThat(createdSodaDTO.getId(), is(equalTo(expectedSodaDto.getId())));
        assertThat(createdSodaDTO.getName(), is(equalTo(expectedSodaDto.getName())));
        assertThat(createdSodaDTO.getQuantity(), is(equalTo(expectedSodaDto.getQuantity())));


    }

    @Test
    void whenAlreadyRegisteredBeerInformed(){
        SodaDTO expectedSodaDto = SodaDTO.builder().build();
        Soda duplicatedSoda = sodaMapper.toModel(expectedSodaDto);

        when(sodaRepository.findByName((expectedSodaDto.getName()))).thenReturn(Optional.of(duplicatedSoda));
         //testa se esta retornando a exceção
        assertThrows(SodaAlreadyIsRegisteredException.class,()->sodaService.createSoda(expectedSodaDto));

    }

    @Test
    void whenNoRegistereSodaIsFoundByName() throws SodaAlreadyIsRegisteredException {
        SodaDTO searchedSodaDTO = SodaDTO.builder().build();
        when(sodaRepository.findByName(searchedSodaDTO.getName())).thenReturn(Optional.empty());
        sodaService.verifyIfExistByName(searchedSodaDTO.getName());
        verify(sodaRepository, times(1)).findByName(searchedSodaDTO.getName());

    }

    @Test
    void whenRegistereSodaIsFoundByNameThrowsException() throws Exception{
        SodaDTO searchedSodaDTO = SodaDTO.builder().build();
        Soda searchedSoda = sodaMapper.toModel(searchedSodaDTO);
        when(sodaRepository.findByName(searchedSodaDTO.getName())).thenReturn(Optional.of(searchedSoda));

        assertThrows(SodaAlreadyIsRegisteredException.class,()->sodaService.verifyIfExistByName(searchedSodaDTO.getName()));
    }

    @Test
    void whenListOfSodaisRequiredReturAList(){
        SodaDTO sodaDTO = SodaDTO.builder().build();
        Soda soda = sodaMapper.toModel(sodaDTO);

        List<Soda> listResponse;

        when(sodaRepository.findAll()).thenReturn(Collections.singletonList(soda));

        assertThat(sodaService.findAllSodas(),is(equalTo(Collections.singletonList(sodaDTO))));

    }

    @Test
    void whenListOfSodaisRequiredReturAEmptyList(){

        List<Soda> listResponse;

        when(sodaRepository.findAll()).thenReturn(Collections.emptyList());

        assertThat(sodaService.findAllSodas(),is(equalTo(Collections.emptyList())));

    }

    @Test
    void whenSodaIsdeleted() throws SodaNotFound {
        SodaDTO sodaDTO = SodaDTO.builder().build();
        Soda soda = sodaMapper.toModel(sodaDTO);
        Long id=1L;
        when(sodaRepository.findById(id)).thenReturn(Optional.of(soda));
        doNothing().when(sodaRepository).delete(soda);
        sodaService.deleteSoda(id);
        verify(sodaRepository, times(1)).findById(id);
        verify(sodaRepository, times(1)).delete(soda);
    }

    @Test
    void whenRequistiondeletedSodaButNotFoud() throws SodaNotFound {
        SodaDTO sodaDTO = SodaDTO.builder().build();
        Soda soda = sodaMapper.toModel(sodaDTO);
        Long id=1L;
        when(sodaRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(SodaNotFound.class,()->sodaService.deleteSoda(id));


    }

    @Test
    void whenRequistionIncrementDone() throws SodaNotFound, SodaStockExceededException {
        SodaDTO sodaDTO = SodaDTO.builder().build();
        Soda soda = sodaMapper.toModel(sodaDTO);
        Soda sodaToIncrement = soda;
        int quantitytoIncrement=1;
        sodaToIncrement.setQuantity(sodaDTO.getMax()-quantitytoIncrement);

        when(sodaRepository.findById(1L)).thenReturn(Optional.of(soda));
        when(sodaRepository.save(sodaToIncrement)).thenReturn(sodaToIncrement);


        assertThat( sodaService.sodaIncrement(1L,quantitytoIncrement),is(equalTo(sodaMapper.toDTO(sodaToIncrement))));

    }

    @Test
    void whenRequistionIncrementIsGreatherThanMax() throws SodaNotFound, SodaStockExceededException {
        SodaDTO sodaDTO = SodaDTO.builder().build();
        Soda soda = sodaMapper.toModel(sodaDTO);
        Soda sodaToIncrement = soda;
        int quantitytoIncrement=1;
        sodaToIncrement.setQuantity(sodaDTO.getMax());

        when(sodaRepository.findById(1L)).thenReturn(Optional.of(soda));

        assertThrows( SodaStockExceededException.class, ()->sodaService.sodaIncrement(1L,quantitytoIncrement));

    }

    @Test
    void whenRequistionDecrementDone() throws SodaNotFound, SodaStockMissException {
        SodaDTO sodaDTO = SodaDTO.builder().build();
        Soda soda = sodaMapper.toModel(sodaDTO);
        Soda sodaToDecrement = soda;
        int quantitytoDecrement=1;
        sodaToDecrement.setQuantity(0+quantitytoDecrement);

        when(sodaRepository.findById(1L)).thenReturn(Optional.of(soda));
        when(sodaRepository.save(sodaToDecrement)).thenReturn(sodaToDecrement);


        assertThat( sodaService.sodadecrement(1L,quantitytoDecrement),is(equalTo(sodaMapper.toDTO(sodaToDecrement))));

    }

    @Test
    void whenRequistionDecrementIsGreatherThanMax() throws SodaStockMissException {
        SodaDTO sodaDTO = SodaDTO.builder().build();
        Soda soda = sodaMapper.toModel(sodaDTO);
        Soda sodaToDecrement = soda;
        int quantityToDecrement=1;
        sodaToDecrement.setQuantity(0);

        when(sodaRepository.findById(1L)).thenReturn(Optional.of(soda));

        assertThrows( SodaStockMissException.class, ()->sodaService.sodadecrement(1L,quantityToDecrement));

    }


}
