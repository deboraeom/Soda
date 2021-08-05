package com.deboraeom.Soda.SodaControllerTest;

import com.deboraeom.Soda.Builder.SodaBuilderDTO;
import com.deboraeom.Soda.Controller.sodaController;
import com.deboraeom.Soda.DTO.QuantityDTO;
import com.deboraeom.Soda.DTO.SodaDTO;
import com.deboraeom.Soda.Exception.SodaNotFound;
import com.deboraeom.Soda.Exception.SodaStockExceededException;
import com.deboraeom.Soda.Exception.SodaStockMissException;
import com.deboraeom.Soda.Service.SodaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;

import static com.deboraeom.Soda.UtilTest.JsonConvertionUtils.asJsonString;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SodaControllerTest {

    private static final String SODA_API_URL_PATH = "/api/v1/soda";
    private static final String SODA_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String SODA_API_SUBPATH_DECREMENT_URL = "/decrement";
    private MockMvc mockMvc;


    @Mock
    private SodaService sodaService;


    @InjectMocks
    private sodaController sodaController;

    @BeforeEach
    void setUp(){
        mockMvc= MockMvcBuilders.standaloneSetup(sodaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
   // esse teste esta falhando, mas ao testar manualmente no postman não falha, tenho que descobrir por que


    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception{


        SodaDTO beerDTO = SodaBuilderDTO.builder().build().toSodaDTO();
        beerDTO.setFlavor(null);

        // then
        mockMvc.perform(post(SODA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(beerDTO)))
                .andExpect(status().isBadRequest());
//        SodaDTO sodaDTO = SodaBuilderDTO.builder().build().toSodaDTO();
//        sodaDTO.setFlavor(null);
//        System.out.println(sodaDTO.toString());
//        mockMvc.perform(post(SODA_API_URL_PATH)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(sodaDTO)))
//                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPOSTIsCalledThenSodaIsCreated() throws Exception{

        SodaDTO sodaDTO= SodaBuilderDTO.builder().build().toSodaDTO();
          when(sodaService.createSoda(sodaDTO)).thenReturn(sodaDTO);
        mockMvc.perform(post(SODA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sodaDTO)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(sodaDTO.getName())))
                .andExpect(jsonPath("$.flavor", is(sodaDTO.getFlavor())))
                .andExpect(jsonPath("$.max", is(sodaDTO.getMax())));
    }

    @Test
    void whenGetListIsCalledThenReturnAListAndReturnStatusOk() throws Exception {
        SodaDTO sodaDTO= SodaBuilderDTO.builder().build().toSodaDTO();
        when(sodaService.findAllSodas()).thenReturn(Collections.singletonList(sodaDTO));
        mockMvc.perform(get(SODA_API_URL_PATH))
                .andExpect(status().isOk());


    }

    @Test
    void whenGetListIsCalledThenReturnAListEmptyAndReturnStatusOk() throws Exception {

        when(sodaService.findAllSodas()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(SODA_API_URL_PATH))
                .andExpect(status().isOk());


    }

    @Test
    void whenGetSodaByNameIsCalledThenReturnASodaAndReturnStatusOk() throws Exception {
        SodaDTO sodaDTO= SodaBuilderDTO.builder().build().toSodaDTO();
        when(sodaService.findByName(sodaDTO.getName())).thenReturn(sodaDTO);
        mockMvc.perform(get(SODA_API_URL_PATH+ "/" + sodaDTO.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(sodaDTO.getName())));

    }
   @Test
    void whenGetSodaByNameIsCalledThentrowsBecauseDoesntExist() throws Exception {
        SodaDTO sodaDTO= SodaBuilderDTO.builder().build().toSodaDTO();
        when(sodaService.findByName(sodaDTO.getName())).thenThrow(SodaNotFound.class);
        mockMvc.perform(get(SODA_API_URL_PATH+ "/" + sodaDTO.getName()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDeleteRequestThenSodaIsDeletedAndReturnStatusNotContent()throws Exception{
        Long id =1L;
      doNothing().when(sodaService).deleteSoda(id);
        mockMvc.perform(delete(SODA_API_URL_PATH+ "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteRequestButSodaDoesntExistThenReturnStatusBadRequs()throws Exception{
        Long id =1L;
        doThrow(SodaNotFound.class).when(sodaService).deleteSoda(id);

        mockMvc.perform(delete(SODA_API_URL_PATH+ "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenIncrementRequestThanIncrementReturnSodaDtoAndHttpStatusOK() throws Exception {

     Long id=1L;
        QuantityDTO quantityDTO = QuantityDTO.builder().build();

        SodaDTO sodaDTO = SodaBuilderDTO.builder().build().toSodaDTO();
        sodaDTO.setQuantity(sodaDTO.getQuantity() + quantityDTO.getQuantity());

        when(sodaService.sodaIncrement(id, quantityDTO.getQuantity())).thenReturn(sodaDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(SODA_API_URL_PATH + "/"
                + id + SODA_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(sodaDTO.getName())))
                .andExpect(jsonPath("$.flavor", is(sodaDTO.getFlavor())))
                .andExpect(jsonPath("$.type", is(sodaDTO.getType().toString())))
                .andExpect(jsonPath("$.quantity", is(sodaDTO.getQuantity())));
    }

    @Test
    void whenIncrementRequestButReturnStockExceeded() throws Exception {

        Long id=1L;
        QuantityDTO quantityDTO = QuantityDTO.builder().build();

        SodaDTO sodaDTO = SodaBuilderDTO.builder().build().toSodaDTO();
        sodaDTO.setQuantity(sodaDTO.getQuantity() + quantityDTO.getQuantity());

        when(sodaService.sodaIncrement(id, quantityDTO.getQuantity())).thenThrow(SodaStockExceededException.class);

        mockMvc.perform(MockMvcRequestBuilders.patch(SODA_API_URL_PATH + "/"
                + id + SODA_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
    }


    @Test
    void whenDecrementRequestThanDecrementReturnSodaDtoAndHttpStatusOK() throws Exception {

        Long id=1L;
        QuantityDTO quantityDTO = QuantityDTO.builder().build();

        SodaDTO sodaDTO = SodaBuilderDTO.builder().build().toSodaDTO();
        sodaDTO.setQuantity(sodaDTO.getQuantity() + quantityDTO.getQuantity());

        when(sodaService.sodadecrement(id, quantityDTO.getQuantity())).thenReturn(sodaDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(SODA_API_URL_PATH + "/"
                + id + SODA_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(sodaDTO.getName())))
                .andExpect(jsonPath("$.flavor", is(sodaDTO.getFlavor())))
                .andExpect(jsonPath("$.type", is(sodaDTO.getType().toString())))
                .andExpect(jsonPath("$.quantity", is(sodaDTO.getQuantity())));
    }

    @Test
    void whenDecrementRequestButReturnStockExceeded() throws Exception {

        Long id=1L;
        QuantityDTO quantityDTO = QuantityDTO.builder().build();

        SodaDTO sodaDTO = SodaBuilderDTO.builder().build().toSodaDTO();
        sodaDTO.setQuantity(sodaDTO.getQuantity() + quantityDTO.getQuantity());

        when(sodaService.sodadecrement(id, quantityDTO.getQuantity())).thenThrow(SodaStockMissException.class);

        mockMvc.perform(MockMvcRequestBuilders.patch(SODA_API_URL_PATH + "/"
                + id + SODA_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
    }












}
