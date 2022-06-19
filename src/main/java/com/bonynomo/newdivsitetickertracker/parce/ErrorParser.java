package com.bonynomo.newdivsitetickertracker.parce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
@Component
public class ErrorParser {

    public String parseErrorFromErrorStream(BufferedReader reader) {
        log.info("Here is the standard error of the command (if any):\n");
        String errorStream = null;
        try {
            while ((errorStream = reader.readLine()) != null) {
                log.error(errorStream);
            }
        } catch (IOException e) {
            log.error("Error while parsing command output", e);
        }
        return errorStream;
    }
}
