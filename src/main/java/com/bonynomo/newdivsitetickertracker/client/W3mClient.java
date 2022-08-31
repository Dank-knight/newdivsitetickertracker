package com.bonynomo.newdivsitetickertracker.client;

import com.bonynomo.newdivsitetickertracker.parse.WemResponseParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class W3mClient {

    private final WemResponseParser commandOutputToStringParser;

    @Autowired
    public W3mClient(WemResponseParser commandOutputToStringParser) {
        this.commandOutputToStringParser = commandOutputToStringParser;
    }

    public Process getW3MEExecProcessByUrl(String url) {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"sh", "-c", "w3m " + url};
        Process proc = null;
        try {
            proc = rt.exec(commands);
        } catch (IOException e) {
            log.error("Error while executing w3m command", e);
        }
        return proc;
    }

    public String getW3mOutputByUrl(String url) {
        Process process = getW3MEExecProcessByUrl(url);
        String output = null;
        if (process != null) {
            output = commandOutputToStringParser.parseOutputOfTheCommand(process);
            String error = commandOutputToStringParser.parseErrorFromErrorStream(process);
            log.info("W3m output: {}", output);
            if (error != null && !error.isEmpty()) {
                log.info("W3m error: {}", error);
            }
        }
        return output;
    }
}
