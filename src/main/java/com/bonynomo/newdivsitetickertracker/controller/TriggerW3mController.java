package com.bonynomo.newdivsitetickertracker.controller;

import com.bonynomo.newdivsitetickertracker.dto.TickerDto;
import com.bonynomo.newdivsitetickertracker.service.W3mService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class TriggerW3mController {

    private final W3mService w3mService;

    @Autowired
    public TriggerW3mController(W3mService w3mService) {
        this.w3mService = w3mService;
    }

    @GetMapping("/dividendstockpile/trigger")
    public String getW3mOutput() {
        String w3mOutput = w3mService.getW3mOutput();
        log.info("W3m output: {}", w3mOutput);
        List<String> titles = w3mService.extractArticleTitles(w3mOutput);
        titles.forEach(title -> log.info("Title: {}", title));
        return w3mOutput;
    }

    @PostMapping("/dividendstockpile/ticker")
    public ResponseEntity<TickerDto> save(@RequestBody TickerDto tickerDto) {
        TickerDto saved = w3mService.save(tickerDto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/dividendstockpile/tickers")
    public ResponseEntity<List<TickerDto>> getAllTickers() {
        List<TickerDto> tickers = w3mService.getAllTickers();
        return ResponseEntity.ok(tickers);
    }
}
