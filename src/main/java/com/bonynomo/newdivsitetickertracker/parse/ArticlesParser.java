package com.bonynomo.newdivsitetickertracker.parse;

import com.bonynomo.newdivsitetickertracker.model.Ticker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticlesParser {

    public static final String THE_REPEATING_LINE = "          [                                             ]\n";

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

    public List<Ticker> extractTickersFromPage(String pageAsString) {
        String dateIntroduced = getPartBetwean("Posted on ", " by ", pageAsString);
        String dirtyTickers = getPartBetwean("The following are new companies on the list this week:", "The others – ", pageAsString);
        String[] aBitCleanerTickers = dirtyTickers.replace("\n", "").replace("\r", "").replace("•", "").split(" ");
        List<String> tickersAsStrings = Arrays.stream(aBitCleanerTickers).filter(s -> !s.isEmpty() && !s.equals(" ")).collect(Collectors.toList());
        ArrayList<Ticker> tickers = new ArrayList<>();
        for (String t : tickersAsStrings) {
            Ticker constructedTicker = Ticker.builder().isActive(true).symbol(t).dateIntroduced(dateIntroduced).build();
            tickers.add(constructedTicker);
        }
        return tickers;
    }

    private String getPartBetwean(String fromPhrase, String toPhrase, String pageAsString) {
        int fromIndex = pageAsString.indexOf(fromPhrase) + fromPhrase.length();
        int toIndex = pageAsString.indexOf(toPhrase);
        return pageAsString.substring(fromIndex, toIndex);
    }

    public String constructMockedPageStringResponse() {
        return "Skip to content\n" +
                "[cropped-Screen-Shot-2021-11-11-at-9]\n" +
                "Menu\n" +
                "\n" +
                "  • Blog\n" +
                "  • Education\n" +
                "  • Company Analysis\n" +
                "  • Portfolio\n" +
                "  • Resources\n" +
                "  • About\n" +
                "\n" +
                "Menu\n" +
                "[the-sign_austin-chan-ukzHlkoz1IE-unsplash]\n" +
                "\n" +
                "10 Undervalued Dividend Growth Stocks To Research The Week of 02/14/2022\n" +
                "\n" +
                "Posted on February 12, 2022 by Jeremy Shirey\n" +
                "\n" +
                "Welcome back to this weekly series reviewing the market and identifying\n" +
                "undervalued dividend growth stocks to research.\n" +
                "\n" +
                "Another week of volatility in the market! Seems like we need to get used to\n" +
                "this type of market movement for a while. The week started off nice and green\n" +
                "but then Thursday and Friday just completely reversed.\n" +
                "\n" +
                "Concerns with inflation, with the CPI coming in at 7.5%, the highest in 40\n" +
                "years, the possibility of a rate hike from the U.S. Federal Reserve earlier\n" +
                "than expected (they meet on Monday) instead of in March, and the tension\n" +
                "between Russia and Ukraine have caused a lot of selling and uncertainty. We\n" +
                "also are in the middle of earnings season so there is increased volatility with\n" +
                "that as well. As DGI investors, this is not necessarily a bad thing, but it\n" +
                "definitely makes some interesting times!\n" +
                "\n" +
                "The S&P 500 ended down 1.82%, Dow down 1.00%, and the NASDAQ down 2.18% for the\n" +
                "week. The overall market is still down for the year as well, down 7.29%, 4.40%,\n" +
                "and 11.85%, respectively.\n" +
                "\n" +
                "With that said, here are some quality dividend growth stocks that are appearing\n" +
                "undervalued based on all 5 of my valuation methods, including:\n" +
                "\n" +
                " 1. Discount to Analyst Price Target\n" +
                " 2. 5% or more off the 52-week high\n" +
                " 3. Discounted Cash Flow (DCF)\n" +
                " 4. P/E Mean Reversion\n" +
                " 5. Dividend Yield Theory (DYT)\n" +
                "\n" +
                "For more information on how to calculate these valuation formulas, check out my\n" +
                "How to Value a Dividend Stock post: https://dividendstockpile.com/\n" +
                "how-to-value-a-dividend-growth-stock/\n" +
                "\n" +
                "[Screen-Shot-2022-02-12-at-10]\n" +
                "\n" +
                "The following are new companies on the list this week:\n" +
                "\n" +
                "  • MRK\n" +
                "  • SBUX\n" +
                "  • TROW\n" +
                "\n" +
                "The others – FNF, HBI, MDT, MMM, SWK, UGI, and WBA continue from last week as\n" +
                "remaining undervalued.\n" +
                "\n" +
                "As I mention each week, valuation is only one part of the decision puzzle that\n" +
                "investors need to evaluate. Other metrics, dividend safety, earnings growth,\n" +
                "payout ratio and other metrics should be considered before investing.\n" +
                "\n" +
                "Thanks for reading and happy investing!\n" +
                "\n" +
                "Share this:\n" +
                "\n" +
                "  • Twitter\n" +
                "  • Facebook\n" +
                "  • \n" +
                "\n" +
                "Leave a Reply Cancel reply\n" +
                "\n" +
                "Your email address will not be published. Required fields are marked *\n" +
                "\n" +
                THE_REPEATING_LINE +
                THE_REPEATING_LINE +
                THE_REPEATING_LINE +
                THE_REPEATING_LINE +
                THE_REPEATING_LINE +
                THE_REPEATING_LINE +
                THE_REPEATING_LINE +
                "Comment * [                                             ]\n" +
                "\n" +
                "Name * [                              ]\n" +
                "\n" +
                "Email * [                              ]\n" +
                "\n" +
                "Website [                              ]\n" +
                "\n" +
                "[ ] Save my name, email, and website in this browser for the next time I\n" +
                "comment.\n" +
                "\n" +
                "[Post Comment] \n" +
                "\n" +
                "Search\n" +
                "[                    ]Search\n" +
                "Sign up to receive notifications of new posts!\n" +
                "\n" +
                "Please leave this field empty[                    ]\n" +
                "Email Address *[                    ]\n" +
                "[Subscribe!]\n" +
                "\n" +
                "Check your inbox or spam folder to confirm your subscription.\n" +
                "\n" +
                "  • @JerShir\n" +
                "\n" +
                "  • August 2022\n" +
                "  • July 2022\n" +
                "  • June 2022\n" +
                "  • May 2022\n" +
                "  • April 2022\n" +
                "  • March 2022\n" +
                "  • February 2022\n" +
                "  • January 2022\n" +
                "  • December 2021\n" +
                "  • November 2021\n" +
                "  • October 2021\n" +
                "\n" +
                "© 2022 | Powered by Superbs Personal Blog theme\n";
    }
}
