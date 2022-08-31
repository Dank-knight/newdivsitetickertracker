package com.bonynomo.newdivsitetickertracker.parse;

import com.bonynomo.newdivsitetickertracker.model.Ticker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticlesParser {

    public static final String NEW_ON_THE_LIST_THIS_WEEK = "New on the list this week:";
    public static final String DISCLAIMER = "This list should be used to begin your research to determine if the stock meets";
    public static final String STOCKS_LISTED = "Stocks Listed:";
    public static final Set<String> ALL_LETTERS_IN_UPPER_CASE = new HashSet<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));

    public List<String> extractArticleTitles(String pageAsString) {
        List<String> allArticleLikeStrings = pageAsString.lines().filter(line -> line.matches("\\d+ Undervalued Dividend Growth Stocks.*") || line.matches("Undervalued Dividend Growth Stocks.*")).collect(Collectors.toList());
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
        } else if (pageAsString.contains(NEW_ON_THE_LIST_THIS_WEEK) && pageAsString.contains("APD, BAC, BR, CMI,")) {
            dirtyTickers = getPartBetween(NEW_ON_THE_LIST_THIS_WEEK, "APD, BAC, BR, CMI,", pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, dirtyTickers, true);
            actionsForTickersLists.get("Save").addAll(tickers);
        } else if (pageAsString.contains("There are no new companies to make the list this week") && pageAsString.contains(DISCLAIMER)) {
            notActiveAnyMoreTickers = getPartBetween("There are no new companies to make the list this week, but the following have\n" + "been removed since the last analysis:", DISCLAIMER, pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, notActiveAnyMoreTickers, false);
            actionsForTickersLists.get("Update").addAll(tickers);
        } else if (pageAsString.contains(NEW_ON_THE_LIST_THIS_WEEK) && pageAsString.contains(DISCLAIMER)) {
            dirtyTickers = getPartBetween(NEW_ON_THE_LIST_THIS_WEEK, "APD", pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, dirtyTickers, true);
            actionsForTickersLists.get("Save").addAll(tickers);
        } else if (pageAsString.contains(STOCKS_LISTED) && pageAsString.contains("New stocks this week: CAH, LOW, NKE, UNP")) {
            dirtyTickers = getPartBetween(STOCKS_LISTED, "New stocks this week: CAH, LOW, NKE, UNP", pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, dirtyTickers, true);
            actionsForTickersLists.get("Save").addAll(tickers);
        } else if (pageAsString.contains(STOCKS_LISTED) && pageAsString.contains(DISCLAIMER)) {
            dirtyTickers = getPartBetween(STOCKS_LISTED, DISCLAIMER, pageAsString);
            List<Ticker> tickers = convertDirtyTickersToListOfTickers(dateIntroduced, dirtyTickers, true);
            actionsForTickersLists.get("Set").addAll(tickers);
        } else {
            throw new IllegalArgumentException("Could not find tickers");
        }
        return actionsForTickersLists;
    }

    public List<Ticker> convertDirtyTickersToListOfTickers(String dateIntroduced, String dirtyTickers, boolean isActive) {
        List<String> tickers = new ArrayList<>();
        for (int i = 0; i < dirtyTickers.length(); i++) {
            tickers.add(String.valueOf(dirtyTickers.charAt(i)));
        }
        boolean isInTheMiddleOfTicker = false;
        StringBuilder ticker = new StringBuilder();
        List<String> tickersList = new ArrayList<>();
        for (String s : tickers) {
            if (!isInTheMiddleOfTicker) {
                if (ALL_LETTERS_IN_UPPER_CASE.contains(s)) {
                    isInTheMiddleOfTicker = true;
                    ticker.append(s);
                }
            } else {
                if (ALL_LETTERS_IN_UPPER_CASE.contains(s)) {
                    ticker.append(s);
                } else {
                    isInTheMiddleOfTicker = false;
                    tickersList.add(ticker.toString());
                    ticker = new StringBuilder();
                }
            }
        }

        List<Ticker> result = new ArrayList<>();
        for (String tickerString : tickersList) {
            result.add(new Ticker(tickerString, dateIntroduced, isActive));
        }
        return result;

    }

    public String getPartBetween(String fromPhrase, String toPhrase, String pageAsString) {
        int fromIndex = pageAsString.indexOf(fromPhrase) + fromPhrase.length();
        int toIndex = pageAsString.indexOf(toPhrase);
        return pageAsString.substring(fromIndex, toIndex);
    }

}
