package com.bonynomo.newdivsitetickertracker.parce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
@Component
public class CommandOutputToStringParser {

    public String parseOutputOfTheCommand(BufferedReader reader) {
        log.info("Here is the standard output of the command:\n");
        String s;
        StringBuilder commandOutput = new StringBuilder();
        try {
            while ((s = reader.readLine()) != null) {
                commandOutput.append(s).append(System.lineSeparator());
            }
        } catch (IOException e) {
            log.error("Error while parsing command output", e);
        }
        String result = commandOutput.toString();
        log.info("Parsing command execution result: {}", result);
        return result;
    }
}