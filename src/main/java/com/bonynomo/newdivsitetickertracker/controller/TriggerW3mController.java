package com.bonynomo.newdivsitetickertracker.controller;

import com.bonynomo.newdivsitetickertracker.service.W3mService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return w3mOutput;
    }
}
