package com.bonynomo.newdivsitetickertracker.scheduled;

import com.bonynomo.newdivsitetickertracker.service.ArticleService;
import com.bonynomo.newdivsitetickertracker.util.SelfMadeJitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ScheduledTasks {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final ArticleService articleService;
    private final SelfMadeJitter selfMadeJitter;

    @Autowired
    public ScheduledTasks(ArticleService articleService, SelfMadeJitter selfMadeJitter) {
        this.articleService = articleService;
        this.selfMadeJitter = selfMadeJitter;
    }

    /**
     * This method is scheduled to run every day  at a random minute between 4:00AM and 4:30AM.
     */
    @Scheduled(cron = "0 ${random.int[0,30]} 4 * * ?")
    public void reportCurrentTime() {
        selfMadeJitter.sleep30To80Sec();
        log.info("The time is now {}", dateFormat.format(new Date()));
        String w3mOutput = articleService.getW3mOutput();
        log.info("W3m output: {}", w3mOutput);
    }

}
