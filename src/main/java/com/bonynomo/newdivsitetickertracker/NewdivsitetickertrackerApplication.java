package com.bonynomo.newdivsitetickertracker;

import com.bonynomo.newdivsitetickertracker.job.W3mCallerJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class NewdivsitetickertrackerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NewdivsitetickertrackerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            JobDetail job1 = JobBuilder.newJob(W3mCallerJob.class).withIdentity("w3mCallerJob", "group1").build();

            Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "group1")
                    .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24)).build();

            Scheduler scheduler1 = new StdSchedulerFactory().getScheduler();
            scheduler1.start();
            scheduler1.scheduleJob(job1, trigger1);

        } catch (SchedulerException e) {
            log.error("Error while scheduling job", e);
        }
    }

}
