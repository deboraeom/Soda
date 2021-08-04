package com.deboraeom.Soda.Builder;

import com.deboraeom.Soda.DTO.SodaDTO;
import com.deboraeom.Soda.Enum.SodaType;
import lombok.Builder;

@Builder
public class SodaBuilderDTO {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Coca Cola";

    @Builder.Default
    private String flavor = "Cola";

    @Builder.Default
    private int max = 10;

    @Builder.Default
    private int quantity = 3;

    @Builder.Default
    private SodaType type = SodaType.NORMAL;

    public SodaDTO toSodaDTO() {
        return new SodaDTO(id,
                name,
                max,
                flavor,
                quantity,
                type);
    }
}
