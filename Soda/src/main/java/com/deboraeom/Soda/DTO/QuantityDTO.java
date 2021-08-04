package com.deboraeom.Soda.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuantityDTO {



    @NotNull
    @Size(min =1, max=200)
    @Column(nullable = false)
    @Max(500)
    private int quantity;

}
