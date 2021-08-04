package com.deboraeom.Soda.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SodaType {
    LIGHT("light"),
    NOSUGAR("no sugar"),
    NORMAL("normal");

    private final String description;

}
