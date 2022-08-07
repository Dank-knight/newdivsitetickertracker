package com.bonynomo.newdivsitetickertracker.controller;

import com.bonynomo.newdivsitetickertracker.dto.TickerDto;
import com.bonynomo.newdivsitetickertracker.service.W3mService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController()
@RequestMapping("dividendstockpile")
public class TriggerW3mController {

    private final W3mService w3mService;

    @Autowired
    public TriggerW3mController(W3mService w3mService) {
        this.w3mService = w3mService;
    }

    @GetMapping("/trigger")
    public String getW3mOutput() {
        String w3mOutput = w3mService.getW3mOutput();
        log.info("W3m output: {}", w3mOutput);
        List<String> titles = w3mService.extractArticleTitles(w3mOutput);
        titles.forEach(title -> log.info("Title: {}", title));
        return w3mOutput;
    }

    @PostMapping("/ticker")
    public ResponseEntity<TickerDto> save(@RequestBody TickerDto tickerDto) {
        TickerDto saved = w3mService.save(tickerDto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/tickers")
    public ResponseEntity<List<TickerDto>> getAllTickers() {
        List<TickerDto> tickers = w3mService.getAllTickers();
        return ResponseEntity.ok(tickers);
    }

    @DeleteMapping("/tickers")
    public ResponseEntity<String> deleteAllTickers() {
        w3mService.deleteAllTickers();
        return ResponseEntity.ok("All tickers deleted");
    }

    @PostMapping("/init")
    public ResponseEntity<String> init() {
        w3mService.init();
        return ResponseEntity.ok("Init done");
    }
}
