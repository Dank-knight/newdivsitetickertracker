package com.bonynomo.newdivsitetickertracker.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Slf4j
@Component
public class SelfMadeJitter {

    private final Random random = new SecureRandom();

    public void sleep30To80Sec() {
        int delay = random.nextInt(50) + 30;
        log.info("The delay is {}", delay);
        try {
            log.info("Sleeping for {} sec", delay);
            Thread.sleep(delay * 1000L);
            log.info("The time is now {}", new Date());
        } catch (InterruptedException e) {
            log.error("Error while sleeping", e);
            Thread.currentThread().interrupt();
        }
    }
}
