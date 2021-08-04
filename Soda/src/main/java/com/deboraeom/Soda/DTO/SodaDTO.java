package com.deboraeom.Soda.DTO;

import com.deboraeom.Soda.Enum.SodaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SodaDTO {
    private Long Id;

    @NotNull
    private String name;


    @NotNull
    @Size(min =1, max=200)
    @Max(500)
    private int max;

    @NotNull
    @Size(min =1, max=200)
    private String flavor;

    @NotNull
    @Max(100)
    private int quantity;

    @NotNull
    private SodaType type;
}
