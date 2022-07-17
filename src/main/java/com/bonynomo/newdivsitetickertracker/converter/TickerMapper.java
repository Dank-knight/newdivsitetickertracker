package com.bonynomo.newdivsitetickertracker.converter;

import com.bonynomo.newdivsitetickertracker.dto.TickerDto;
import com.bonynomo.newdivsitetickertracker.model.Ticker;
import org.mapstruct.Mapper;

@Mapper
public interface TickerMapper {

    TickerMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(TickerMapper.class);

    Ticker toTicker(TickerDto tickerDto);
    TickerDto toTickerDto(Ticker ticker);

}
