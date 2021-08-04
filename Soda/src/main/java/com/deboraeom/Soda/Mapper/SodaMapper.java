package com.deboraeom.Soda.Mapper;


import com.deboraeom.Soda.DTO.SodaDTO;
import com.deboraeom.Soda.Entity.Soda;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SodaMapper {

    SodaMapper INSTANCE = Mappers.getMapper(SodaMapper.class);

    Soda toModel(SodaDTO sodaDTO);

    SodaDTO toDTO(Soda soda);

}
