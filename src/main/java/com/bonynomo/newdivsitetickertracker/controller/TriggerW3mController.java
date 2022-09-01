package com.bonynomo.newdivsitetickertracker.controller;

import com.bonynomo.newdivsitetickertracker.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("dividendstockpile")
public class TriggerW3mController {

    private final ArticleService articleService;

    @Autowired
    public TriggerW3mController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/trigger")
    public String getW3mOutput() {
        return "trigger check of new article";
    }

    @PostMapping("/init")
    public ResponseEntity<String> init() {
        articleService.init();
        return ResponseEntity.ok("Init done");
    }
}
