package com.bonynomo.newdivsitetickertracker.service;

import com.bonynomo.newdivsitetickertracker.client.W3mClient;
import com.bonynomo.newdivsitetickertracker.converter.TickerMapper;
import com.bonynomo.newdivsitetickertracker.dto.TickerDto;
import com.bonynomo.newdivsitetickertracker.exception.UnableToInitTickersException;
import com.bonynomo.newdivsitetickertracker.model.Ticker;
import com.bonynomo.newdivsitetickertracker.parse.ArticlesParser;
import com.bonynomo.newdivsitetickertracker.parse.WemResponseParser;
import com.bonynomo.newdivsitetickertracker.repo.TickerRepo;
import com.bonynomo.newdivsitetickertracker.util.SelfMadeJitter;
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
    private final TickerRepo tickerRepo;
    private final ArticlesParser articlesParser;
    private final W3mClient w3mClient;
    private final SelfMadeJitter jitter;

    @Autowired
    public W3mService(TickerRepo tickerRepo, ArticlesParser articlesParser, W3mClient w3mClient, SelfMadeJitter jitter) {
        this.tickerRepo = tickerRepo;
        this.articlesParser = articlesParser;
        this.w3mClient = w3mClient;
        this.jitter = jitter;
    }

    public String getW3mOutput() {
        return w3mClient.getW3mOutputByUrl(url);
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
        List<String> allArticlesBeforeInitialArticle = getAllArticlesBeforeInitialArticle();
        List<String> reversedUrlsSoTheyAreInChronologicalOrder = prepareUrlsByArticles(allArticlesBeforeInitialArticle);
        for (String constructedUrl : reversedUrlsSoTheyAreInChronologicalOrder) {
            jitter.sleep30To80Sec();
            log.info("Constructed url: {}", constructedUrl);
            String pageAsString = w3mClient.getW3mOutputByUrl(constructedUrl);
            List<Ticker> newUndervaluedTickers = articlesParser.extractTickersFromPage(pageAsString);
            tickerRepo.saveAll(newUndervaluedTickers);
        }
    }

    private List<String> prepareUrlsByArticles(List<String> allArticlesBeforeInitialArticle) {
        allArticlesBeforeInitialArticle = allArticlesBeforeInitialArticle.stream().map(String::toLowerCase).collect(Collectors.toList());
        log.debug("Found {} articles before initial article", allArticlesBeforeInitialArticle.size());
        List<String> urlsToProcess = transformArticlesToUrls(allArticlesBeforeInitialArticle);
        return reverseList(urlsToProcess);
    }

    private List<String> reverseList(List<String> urlsToProcess) {
        List<String> reversedUrlsSoTheyAreInChronologicalOrder = new ArrayList<>();
        for (int i = urlsToProcess.size() - 1; i >= 0; i--) {
            reversedUrlsSoTheyAreInChronologicalOrder.add(urlsToProcess.get(i));
        }
        return reversedUrlsSoTheyAreInChronologicalOrder;
    }

    private List<String> transformArticlesToUrls(List<String> titles) {
        List<String> urls = new ArrayList<>();
        for (String title : titles) {
            urls.add(urlFromTitle(title));
        }
        return urls;
    }

    private String urlFromTitle(String title) {
        return url + title.replace(" ", "-").replace("\\(", "").replace("\\)", "").replace("\\/", "-") + "/";
    }

    private List<String> createMockedTitles() {
        List<String> strings = new ArrayList<>();
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (8/8/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (8/1/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (7/25/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (07/18/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (07/11/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (6/20/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (06/13/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (06/06/2022)");
        strings.add("Undervalued Dividend Growth Stocks To Research This Week (05/30/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (05/23/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (05/16/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (05/09/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (04/25/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (04/17/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (04/04/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (03/27/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (03/14/2022)");
        strings.add("Undervalued Dividend Growth Stocks to Research This Week (03/07/2022)");
        strings.add("16 Undervalued Dividend Growth Stocks for the Week of 02/28/2022");
        strings.add("11 Undervalued Dividend Growth Stocks for the Week of 02/21/2022");
        strings.add("10 Undervalued Dividend Growth Stocks To Research The Week of 02/14/2022");
        List<String> lowerCaseStrings = new ArrayList<>();
        for (String s : strings) {
            lowerCaseStrings.add(s.toLowerCase());
        }
        return lowerCaseStrings;
    }

    private List<String> getAllArticlesBeforeInitialArticle() {
        boolean isInitArticleReqached = false;
        ArrayList<String> titles = new ArrayList<>();
        String urlToSerach = this.url;
        int page = 1;
        while (!isInitArticleReqached) {
            if (page > 1) {
                urlToSerach = this.url + "page/" + page;
            }
            jitter.sleep30To80Sec();
            String w3mOutputByUrl = w3mClient.getW3mOutputByUrl(urlToSerach);
            List<String> extractedTitles = articlesParser.extractArticleTitles(w3mOutputByUrl);
            titles.addAll(extractedTitles);
            page++;
            if (extractedTitles.contains("10 Undervalued Dividend Growth Stocks To Research The Week of 02/14/2022")) {
                isInitArticleReqached = true;
            }
        }
        return titles;
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
