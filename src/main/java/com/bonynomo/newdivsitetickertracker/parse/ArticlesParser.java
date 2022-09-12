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

    private static final String NEW_ON_THE_LIST_THIS_WEEK = "New on the list this week:";
    private static final String DISCLAIMER = "This list should be used to begin your research to determine if the stock meets";
    private static final String STOCKS_LISTED = "Stocks Listed:";
    private static final Set<String> ALL_LETTERS_IN_UPPER_CASE = new HashSet<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
    private final Map<String, String> monthNumberByName;

    public ArticlesParser() {
        monthNumberByName = new HashMap<>();
        monthNumberByName.put("January", "01");
        monthNumberByName.put("February", "02");
        monthNumberByName.put("March", "03");
        monthNumberByName.put("April", "04");
        monthNumberByName.put("May", "05");
        monthNumberByName.put("June", "06");
        monthNumberByName.put("July", "07");
        monthNumberByName.put("August", "08");
        monthNumberByName.put("September", "09");
        monthNumberByName.put("October", "10");
        monthNumberByName.put("November", "11");
        monthNumberByName.put("December", "12");
    }

    public List<String> extractUndervaluedDividendGrowthStocksArticleTitles(String pageAsString) {
        List<String> allArticlesAsStrings = pageAsString.lines().filter(line -> line.matches("\\d+ Undervalued Dividend Growth Stocks.*") || line.matches("Undervalued Dividend Growth Stocks.*")).collect(Collectors.toList());
        log.debug("Found {} article like strings", allArticlesAsStrings.size());
        return cleanUpArticleTitles(allArticlesAsStrings);
    }

    private List<String> cleanUpArticleTitles(List<String> titles) {
        log.debug("Clean up article titles: {}", titles);
        titles.removeIf(title -> title.startsWith("[cropped-"));
        titles.removeIf(title -> title.startsWith("[Subscribe"));
        log.debug("Clean up article titles: {}", titles);
        return titles;
    }

    public Map<String, List<Ticker>> extractTickersFromPage(String pageAsString) {
        String tawDateIntroduced = getPartBetween("Posted on ", " by ", pageAsString);
        log.debug("Raw date introduced: {}", tawDateIntroduced);
        String dateIntroduced = toDayMonthYear(tawDateIntroduced);
        log.debug("Date introduced: {}", tawDateIntroduced);
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

    public String toDayMonthYear(String tawDateIntroduced) {
        String[] split = tawDateIntroduced.split(" ");
        String day = split[1].replace(",", "");
        if (day.length() == 1) {
            day = "0" + day;
        }
        String month = String.valueOf(monthNumberByName.get(split[0]));
        String year = split[2];
        return day + "." + month + "." + year;
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

    public String extractLatestArticleTitle(String pageAsString) {
        if (pageAsString.contains("Posted on") && pageAsString.contains("]")) {
            int indexOfEndOfFirstArticle = pageAsString.indexOf("Posted on");
            for (int i = indexOfEndOfFirstArticle; i >= 0; i--) {
                if (pageAsString.charAt(i) == ']') {
                    return pageAsString.substring(i + 1, indexOfEndOfFirstArticle);
                }
            }
        }
        throw new IllegalArgumentException("Could not find latest article title");
    }
}
