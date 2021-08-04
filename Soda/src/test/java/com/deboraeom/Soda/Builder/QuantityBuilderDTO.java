package com.deboraeom.Soda.Builder;

import com.deboraeom.Soda.DTO.QuantityDTO;
import com.deboraeom.Soda.DTO.SodaDTO;
import com.deboraeom.Soda.Enum.SodaType;
import lombok.Builder;

@Builder
public class QuantityBuilderDTO {
    @Builder.Default
    private int quantity = 50;



    public QuantityDTO toQuantityDTO() {
        return new QuantityDTO(
                quantity
                );
    }

}
