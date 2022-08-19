package com.bonynomo.newdivsitetickertracker.parse;

import com.bonynomo.newdivsitetickertracker.model.Ticker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticlesParser {

    public static final String NEW_ON_THE_LIST_THIS_WEEK = "New on the list this week:";
    public static final String DISCLAMER = "This list should be used to begin your research to determine if the stock meets";

    public List<String> extractArticleTitles(String pageAsString) {
        List<String> allArticleLikeStrings = pageAsString.lines()
                .filter(line -> line.matches("\\d+ Undervalued Dividend Growth Stocks.*") || line.matches("Undervalued Dividend Growth Stocks.*"))
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

    public Map<String, List<Ticker>> extractTickersFromPage(String pageAsString) {
        String dateIntroduced = getPartBetween("Posted on ", " by ", pageAsString);
        String dirtyTickers;
        String notActiveAnyMoreTickers;
        Map<String, List<Ticker>> actionsForTickersLists = new HashMap<>();
        actionsForTickersLists.put("Save", new ArrayList<>());
        actionsForTickersLists.put("Update", new ArrayList<>());
        actionsForTickersLists.put("Set", new ArrayList<>());
        if (pageAsString.contains("The following are new companies on the list this week:") && pageAsString.contains("The others – ")) {
            dirtyTickers = getPartBetween("The following are new companies on the list this week:", "The others – ", pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, dirtyTickers, true);
            actionsForTickersLists.get("Save").addAll(tickers);
        } else if (pageAsString.contains("The new companies to make the list this week are:") && pageAsString.contains("The remaining names ")) {
            dirtyTickers = getPartBetween("The new companies to make the list this week are:", "The remaining names ", pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, dirtyTickers, true);
            actionsForTickersLists.get("Save").addAll(tickers);
        } else if (pageAsString.contains(NEW_ON_THE_LIST_THIS_WEEK) && pageAsString.contains("APD, BAC, BR, CMI, FNF, HBI, JPM, LEG, MDT, MMM, SBUX, SWK, TROW, UGI, V, and\n" +
                "WBA continue from last week to look undervalued.")) {
            dirtyTickers = getPartBetween(NEW_ON_THE_LIST_THIS_WEEK, "APD, BAC, BR, CMI, FNF, HBI, JPM, LEG, MDT, MMM, SBUX, SWK, TROW, UGI, V, and\n" +
                    "WBA continue from last week to look undervalued.", pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, dirtyTickers, true);
            actionsForTickersLists.get("Save").addAll(tickers);
        } else if (pageAsString.contains("There are no new companies to make the list this week") && pageAsString.contains(DISCLAMER)) {
            notActiveAnyMoreTickers = getPartBetween("There are no new companies to make the list this week, but the following have\n" +
                    "been removed since the last analysis:", DISCLAMER, pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, notActiveAnyMoreTickers, false);
            actionsForTickersLists.get("Update").addAll(tickers);
        } else if (pageAsString.contains(NEW_ON_THE_LIST_THIS_WEEK) && pageAsString.contains(DISCLAMER)) {
            dirtyTickers = getPartBetween(NEW_ON_THE_LIST_THIS_WEEK, "APD", pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, dirtyTickers, true);
            actionsForTickersLists.get("Save").addAll(tickers);
        } else if (pageAsString.contains("Stocks Listed:") && pageAsString.contains(DISCLAMER)) {
            dirtyTickers = getPartBetween("Stocks Listed:", DISCLAMER, pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, dirtyTickers, true);
            actionsForTickersLists.get("Set").addAll(tickers);
        } else {
            throw new IllegalArgumentException("Could not find tickers");
        }
        return actionsForTickersLists;
    }

    private List<Ticker> convertDirtyTickersToListOfTickers(String dateIntroduced, String dirtyTickers, boolean isActive) {
        String[] aBitCleanerTickers = dirtyTickers.replace("\n", "").replace("\r", "").replace("•", "").replace("and", "").split(" ");
        List<String> tickersAsStrings = Arrays.stream(aBitCleanerTickers).filter(s -> !s.isEmpty() && !s.equals(" ")).collect(Collectors.toList());
        ArrayList<Ticker> tickers = new ArrayList<>();
        for (String t : tickersAsStrings) {
            Ticker constructedTicker = Ticker.builder()
                    .isActive(isActive)
                    .symbol(t)
                    .dateIntroduced(dateIntroduced)
                    .build();
            tickers.add(constructedTicker);
        }
        return tickers;
    }

    private String getPartBetween(String fromPhrase, String toPhrase, String pageAsString) {
        int fromIndex = pageAsString.indexOf(fromPhrase) + fromPhrase.length();
        int toIndex = pageAsString.indexOf(toPhrase);
        return pageAsString.substring(fromIndex, toIndex);
    }

}
