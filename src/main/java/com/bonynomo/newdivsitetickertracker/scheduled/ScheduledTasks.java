package com.bonynomo.newdivsitetickertracker.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ScheduledTasks {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // to be started every day at a random minute between 4:00AM and 4:30AM
    @Scheduled(cron="0 ${random.int[0,30]} 4 * * ?")
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

}
