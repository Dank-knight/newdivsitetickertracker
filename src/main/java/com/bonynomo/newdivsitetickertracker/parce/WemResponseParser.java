package com.bonynomo.newdivsitetickertracker.parce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Component
public class WemResponseParser {

    public String parseOutputOfTheCommand(Process process) {
        log.info("Here is the standard output of the command:\n");
        return getResultStringFormBufferedReader(new BufferedReader(new InputStreamReader(process.getInputStream())));
    }

    public String parseErrorFromErrorStream(Process process) {
        log.info("Here is the standard error of the command (if any):\n");
        return getResultStringFormBufferedReader(new BufferedReader(new InputStreamReader(process.getErrorStream())));
    }

    public String getResultStringFormBufferedReader(BufferedReader reader) {
        String line;
        StringBuilder commandOutput = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                commandOutput.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            log.error("Error while parsing command output", e);
        }
        String result = commandOutput.toString();
        log.info("Parsing command execution result: {}", result);
        return result;
    }
}