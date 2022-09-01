package com.bonynomo.newdivsitetickertracker.controller;

import com.bonynomo.newdivsitetickertracker.dto.TickerDto;
import com.bonynomo.newdivsitetickertracker.service.ArticleService;
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
@RestController
@RequestMapping("tickers")
public class TickerController {

    private final ArticleService articleService;

    @Autowired
    public TickerController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/ticker")
    public ResponseEntity<TickerDto> save(@RequestBody TickerDto tickerDto) {
        TickerDto saved = articleService.save(tickerDto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<TickerDto>> getAllTickers() {
        List<TickerDto> tickers = articleService.getAllTickers();
        return ResponseEntity.ok(tickers);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllTickers() {
        articleService.deleteAllTickers();
        return ResponseEntity.ok("All tickers deleted");
    }

    @GetMapping("/active")
    public ResponseEntity<List<TickerDto>> getAllActiveTickers() {
        List<TickerDto> tickers = articleService.getAllActiveTickers();
        return ResponseEntity.ok(tickers);
    }

}
