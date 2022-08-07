package com.bonynomo.newdivsitetickertracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TickerDto {

    private String id;
    private String symbol;
    private String dateIntroduced;
    private Boolean isActive;
}
