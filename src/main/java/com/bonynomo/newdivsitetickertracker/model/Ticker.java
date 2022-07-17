package com.bonynomo.newdivsitetickertracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticker {

    @Id
    private String id;
    private String symbol;
    private String dateIntroduced;
    private boolean isActive;
}
