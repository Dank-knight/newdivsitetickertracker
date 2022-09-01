package com.bonynomo.newdivsitetickertracker.repo;

import com.bonynomo.newdivsitetickertracker.model.Ticker;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TickerRepo extends MongoRepository<Ticker, String> {

    Ticker findBySymbol(String symbol);

    List<Ticker> findAllByIsActiveTrue();
}
