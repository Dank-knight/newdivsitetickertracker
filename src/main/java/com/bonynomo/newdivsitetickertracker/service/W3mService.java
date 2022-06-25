package com.bonynomo.newdivsitetickertracker.service;

import com.bonynomo.newdivsitetickertracker.parce.WemResponseParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class W3mService {

    @Value("${website}")
    private String url;
    private final WemResponseParser commandOutputToStringParser;

    @Autowired
    public W3mService(WemResponseParser commandOutputToStringParser) {
        this.commandOutputToStringParser = commandOutputToStringParser;
    }

    public String getW3mOutput() {
        Process process = getW3MEExecProcess();
        String output = null;
        if (process != null) {
            output = commandOutputToStringParser.parseOutputOfTheCommand(process);
            String error = commandOutputToStringParser.parseErrorFromErrorStream(process);
            log.info("W3m output: {}", output);
            log.info("W3m error: {}", error);
        }
        return output;
    }

    private Process getW3MEExecProcess() {
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
}
