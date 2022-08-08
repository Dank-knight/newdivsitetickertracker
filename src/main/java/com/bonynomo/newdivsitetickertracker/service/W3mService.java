package com.bonynomo.newdivsitetickertracker.service;

import com.bonynomo.newdivsitetickertracker.converter.TickerMapper;
import com.bonynomo.newdivsitetickertracker.dto.TickerDto;
import com.bonynomo.newdivsitetickertracker.exception.UnableToInitTickersException;
import com.bonynomo.newdivsitetickertracker.model.Ticker;
import com.bonynomo.newdivsitetickertracker.parce.WemResponseParser;
import com.bonynomo.newdivsitetickertracker.repo.TickerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class W3mService {

    //https://dividendstockpile.com/10-undervalued-dividend-growth-stocks-to-research-the-week-of-02-14-2022/
    public static final String DATE_INTRODUCED = "14.02.2022";

    @Value("${website}")
    private String url;
    private final WemResponseParser commandOutputToStringParser;
    private final TickerRepo tickerRepo;

    @Autowired
    public W3mService(WemResponseParser commandOutputToStringParser, TickerRepo tickerRepo) {
        this.commandOutputToStringParser = commandOutputToStringParser;
        this.tickerRepo = tickerRepo;
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

    public List<String> extractArticleTitles(String pageAsString) {
        List<String> allArticleLikeStrings = pageAsString.lines()
                .filter(line -> line.startsWith("[") && line.endsWith("]"))
                .collect(Collectors.toList());
        log.debug("Found {} article like strings", allArticleLikeStrings.size());
        return cleanUpArticleTitles(allArticleLikeStrings);
    }

    private List<String> cleanUpArticleTitles(List<String> titles) {
        log.debug("Clean up article titles: {}", titles);
        titles.removeIf(title -> title.startsWith("[cropped-"));
        titles.removeIf(title -> title.startsWith("[Subscribe"));
        log.debug("Clean up article titles: {}", titles);
        return titles;
    }

    public TickerDto save(TickerDto tickerDto) {
        Ticker ticker = TickerMapper.INSTANCE.tickerDtoToTicker(tickerDto);
        if (tickerRepo.findBySymbol(ticker.getSymbol()) == null) {
            Ticker saved = tickerRepo.save(ticker);
            log.debug("Saved ticker: {}", saved);
            return TickerMapper.INSTANCE.tickerToTickerDto(saved);
        } else {
            throw new IllegalArgumentException("Ticker already exists");
        }
    }

    public List<TickerDto> getAllTickers() {
        List<Ticker> tickers = tickerRepo.findAll();
        log.debug("Found {} tickers", tickers.size());
        List<TickerDto> tickerDtos = new ArrayList<>();
        for (Ticker t : tickers) {
            TickerDto tickerDto = TickerMapper.INSTANCE.tickerToTickerDto(t);
            tickerDtos.add(tickerDto);
        }
        log.debug("Found {} tickers", tickerDtos.size());
        return tickerDtos;
    }

    public void deleteAllTickers() {
        log.debug("Deleting all tickers");
        tickerRepo.deleteAll();
    }

    public void init() {
        deleteAllTickers();
        saveTickersFromInitialArticle();
    }

    private void saveTickersFromInitialArticle() {
        log.debug("Starting to parse initial tickers from resources file");
        List<String> strings = parseInitTickers();
        for (String string : strings) {
            Ticker ticker = new Ticker();
            ticker.setSymbol(string);
            ticker.setDateIntroduced(DATE_INTRODUCED);
            ticker.setIsActive(true);
            tickerRepo.save(ticker);
            log.debug("Saved ticker: {}", ticker);
        }
        log.debug("Finished parsing initial tickers from resources file");
    }

    private List<String> parseInitTickers() {
        try {
            File file = loadInitTickersFile();
            log.debug("Parsing initial tickers from file: {}", file.getAbsolutePath());
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                return br.lines().collect(Collectors.toList());
            }
        } catch (IOException e) {
            log.error("Error while loading initial tickers", e);
            throw new UnableToInitTickersException("Error while loading initial tickers", e);
        }
    }

    private File loadInitTickersFile() throws FileNotFoundException {
        return ResourceUtils.getFile("classpath:" + "initial_tickers.txt");
    }
}
