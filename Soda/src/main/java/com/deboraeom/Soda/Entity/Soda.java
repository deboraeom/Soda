package com.deboraeom.Soda.Entity;

import com.deboraeom.Soda.Enum.SodaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="soda")
@Entity(name="Soda")
public class Soda {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int max;

    @Column(nullable = false)
    private String flavor;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SodaType type;

}
