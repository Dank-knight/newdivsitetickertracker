package com.bonynomo.newdivsitetickertracker.converter;

import com.bonynomo.newdivsitetickertracker.converter.config.CentralMapperConfig;
import com.bonynomo.newdivsitetickertracker.dto.TickerDto;
import com.bonynomo.newdivsitetickertracker.model.Ticker;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(config = CentralMapperConfig.class)
@Component
public interface TickerMapper {

    TickerMapper INSTANCE = Mappers.getMapper(TickerMapper.class);

    Ticker tickerDtoToTicker(TickerDto tickerDto);

    TickerDto tickerToTickerDto(Ticker ticker);

}
