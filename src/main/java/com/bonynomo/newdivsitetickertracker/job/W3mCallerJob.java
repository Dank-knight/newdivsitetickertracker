package com.bonynomo.newdivsitetickertracker.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class W3mCallerJob implements Job {

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"sh", "-c", "w3m https://dividendstockpile.com/"};
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        // Read the output from the command
        log.info("Here is the standard output of the command:\n");
        String s;
        StringBuilder commandOutput = new StringBuilder();
        while ((s = stdInput.readLine()) != null) {
            commandOutput.append(s).append(System.lineSeparator());
        }
        log.info(commandOutput.toString());

        // Read any errors from the attempted command
        log.info("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            log.info(s);
        }

    }
}
