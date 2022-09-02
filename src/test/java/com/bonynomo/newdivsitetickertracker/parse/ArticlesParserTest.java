package com.bonynomo.newdivsitetickertracker.parse;

import com.bonynomo.newdivsitetickertracker.model.Ticker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

class ArticlesParserTest {

    private static final String DATE_INTRODUCED = "01/01/2020";

    private final ArticlesParser articlesParser = new ArticlesParser();

    @Test
    void when_PageContainsTickersDividedByBulletPoints_expect_tickersAreExtracted() {
        String pageAsString = "The following are new companies on the list this week: \n" +
                "• MRK\n" +
                "• SBUX\n" +
                "• TROW\n" +
                "\n" +
                "The others – FNF, HBI, MDT, MMM, SWK, UGI, and WBA continue from last week as\n" +
                "remaining undervalued.\n" +
                "\n" +
                "As I mention each week, valuation is only one part of the decision puzzle that\n" +
                "investors need to evaluate. Other metrics, dividend safety, earnings growth,\n" +
                "payout ratio and other metrics should be considered before investing.";


        String partBetween = articlesParser.getPartBetween("The following are new companies on the list this week:", "The others – ", pageAsString);
        List<Ticker> tickers = articlesParser.convertDirtyTickersToListOfTickers(DATE_INTRODUCED, partBetween, true);
        assertThat(tickers, hasSize(3));
        Ticker fTicker = tickers.get(0);
        Ticker sTicker = tickers.get(1);
        Ticker tTicker = tickers.get(2);
        assertThat(fTicker.getSymbol(), is("MRK"));
        assertThat(sTicker.getSymbol(), is("SBUX"));
        assertThat(tTicker.getSymbol(), is("TROW"));
    }

    @Test
    void when_PageContainsTickersDividedByComasAndAndWord_expect_tickersAreExtracted() {
        String pageAsString = "The new companies to make the list this week are:\n" +
                "\n" +
                "BAC, BR, CMI, MSM, TSCO, UGI, and WBA\n" +
                "\n" +
                "The remaining names – FNF, HBI, IIPR, LEG, MDT, MMM, SBUX, SWK, and TROW are a\n" +
                "continuation of last week and remain undervalued.\n" +
                "\n" +
                "This list should be used to begin your research to determine if the stock meets\n" +
                "all of your investment goals and criteria. Valuation should only be one of many\n" +
                "aspects you look at when deciding to make an investment.";

        String partBetween = articlesParser.getPartBetween("The new companies to make the list this week are:", "The remaining names – ", pageAsString);
        List<Ticker> tickers = articlesParser.convertDirtyTickersToListOfTickers(DATE_INTRODUCED, partBetween, true);
        assertThat(tickers, hasSize(7));
        Ticker fTicker = tickers.get(0);
        Ticker sTicker = tickers.get(1);
        Ticker tTicker = tickers.get(2);
        Ticker foTicker = tickers.get(3);
        Ticker fiTicker = tickers.get(4);
        Ticker siTicker = tickers.get(5);
        Ticker seTicker = tickers.get(6);
        assertThat(fTicker.getSymbol(), is("BAC"));
        assertThat(sTicker.getSymbol(), is("BR"));
        assertThat(tTicker.getSymbol(), is("CMI"));
        assertThat(foTicker.getSymbol(), is("MSM"));
        assertThat(fiTicker.getSymbol(), is("TSCO"));
        assertThat(siTicker.getSymbol(), is("UGI"));
        assertThat(seTicker.getSymbol(), is("WBA"));
    }

    @Test
    void when_PageContainsTickersDividedByComasAnd_expect_tickersAreExtracted() {
        String pageAsString = "The new companies to make the list this week are:\n" +
                "\n" +
                "AVGO, BLK, IP, JPM, PRU, TXN, V\n" +
                "\n" +
                "The remaining names – BR, CMI, FNF, HBI, IIPR, LEG, MDT, MMM, MSM, SBUX, SWK,\n" +
                "TROW, TSCO, and UGI are a continuation from last week and remain undervalued.\n" +
                "\n" +
                "This list should be used to begin your research to determine if the stock meets\n" +
                "all of your investment goals and criteria. Valuation should only be one of many\n" +
                "aspects you look at when deciding to make an investment.";

        String partBetween = articlesParser.getPartBetween("The new companies to make the list this week are:", "The remaining names – ", pageAsString);
        List<Ticker> tickers = articlesParser.convertDirtyTickersToListOfTickers(DATE_INTRODUCED, partBetween, true);
        assertThat(tickers, hasSize(7));
        Ticker fTicker = tickers.get(0);
        Ticker sTicker = tickers.get(1);
        Ticker tTicker = tickers.get(2);
        Ticker foTicker = tickers.get(3);
        Ticker fiTicker = tickers.get(4);
        Ticker siTicker = tickers.get(5);
        Ticker seTicker = tickers.get(6);
        assertThat(fTicker.getSymbol(), is("AVGO"));
        assertThat(sTicker.getSymbol(), is("BLK"));
        assertThat(tTicker.getSymbol(), is("IP"));
        assertThat(foTicker.getSymbol(), is("JPM"));
        assertThat(fiTicker.getSymbol(), is("PRU"));
        assertThat(siTicker.getSymbol(), is("TXN"));
        assertThat(seTicker.getSymbol(), is("V"));
    }

    @Test
    void when_PageContainsTickersDividedByComasAndHasAnExtraComaInTheEnd_expect_tickersAreExtracted() {

        String pageAsString = "New on the list this week: AOS, AVGO, BLK, INTC, TXN,\n" +
                "\n" +
                "APD, BAC, BR, CMI, DLR, FNF, HBI, HD, IIPR, JPM, LEG, MDT, MMM, MSM, SBUX, SWK,\n" +
                "TROW, TSCO, UGI, V, and WBA continue from the last report and continue to look\n" +
                "undervalued.\n" +
                "\n" +
                "This list should be used to begin your research to determine if the stock meets\n" +
                "all of your investment goals and criteria. Valuation should only be one of many\n" +
                "aspects you look at when deciding to make an investment.";
        String partBetween = articlesParser.getPartBetween("New on the list this week:", "APD, BAC, BR, CMI,", pageAsString);
        List<Ticker> tickers = articlesParser.convertDirtyTickersToListOfTickers(DATE_INTRODUCED, partBetween, true);
        assertThat(tickers, hasSize(5));
        Ticker fTicker = tickers.get(0);
        Ticker sTicker = tickers.get(1);
        Ticker tTicker = tickers.get(2);
        Ticker foTicker = tickers.get(3);
        Ticker fiTicker = tickers.get(4);

        assertThat(fTicker.getSymbol(), is("AOS"));
        assertThat(sTicker.getSymbol(), is("AVGO"));
        assertThat(tTicker.getSymbol(), is("BLK"));
        assertThat(foTicker.getSymbol(), is("INTC"));
        assertThat(fiTicker.getSymbol(), is("TXN"));
    }

    @Test
    void when_PageContainsMultipleNewLineCharsbetweenTickers_expect_tickersAreExtracted() {
        String pageAsString = "Stocks Listed: AOS, APD, AVGO, BAC, BLK, BR, DLR, FNF, HBI, HD, IIPR, INTC,\n" +
                "JPM, LEG, MDT, MMM, MSM, RY, SBUX, SMG, SPG, STOR, SWK, TD, TROW, TSCO, TXN,\n" +
                "UGI, V, WBA\n" +
                "\n" +
                "This list should be used to begin your research to determine if the stock meets\n" +
                "all of your investment goals and criteria. Valuation should only be one of many\n" +
                "aspects you look at when deciding to make an investment.";
        String partBetween = articlesParser.getPartBetween("Stocks Listed:", "This list should be used to begin your research to determine if the stock meets", pageAsString);
        List<Ticker> tickers = articlesParser.convertDirtyTickersToListOfTickers(DATE_INTRODUCED, partBetween, true);
        assertThat(tickers, hasSize(30));
        Ticker fTicker = tickers.get(0);
        Ticker sTicker = tickers.get(1);
        Ticker tTicker = tickers.get(2);
        Ticker foTicker = tickers.get(3);
        Ticker fiTicker = tickers.get(4);
        Ticker siTicker = tickers.get(5);
        Ticker seTicker = tickers.get(6);
        Ticker eiTicker = tickers.get(7);
        Ticker niTicker = tickers.get(8);
        Ticker tiTicker = tickers.get(9);
        Ticker fiTicker2 = tickers.get(10);
        Ticker siTicker2 = tickers.get(11);
        Ticker seTicker2 = tickers.get(12);
        Ticker eiTicker2 = tickers.get(13);
        Ticker niTicker2 = tickers.get(14);
        Ticker tiTicker2 = tickers.get(15);
        Ticker fiTicker3 = tickers.get(16);
        Ticker siTicker3 = tickers.get(17);
        Ticker seTicker3 = tickers.get(18);
        Ticker eiTicker3 = tickers.get(19);
        Ticker niTicker3 = tickers.get(20);
        Ticker tiTicker3 = tickers.get(21);
        Ticker fiTicker4 = tickers.get(22);
        Ticker siTicker4 = tickers.get(23);
        Ticker seTicker4 = tickers.get(24);
        Ticker eiTicker4 = tickers.get(25);
        Ticker niTicker4 = tickers.get(26);
        Ticker tiTicker4 = tickers.get(27);
        Ticker fiTicker5 = tickers.get(28);
        Ticker siTicker5 = tickers.get(29);
        assertThat(fTicker.getSymbol(), is("AOS"));
        assertThat(sTicker.getSymbol(), is("APD"));
        assertThat(tTicker.getSymbol(), is("AVGO"));
        assertThat(foTicker.getSymbol(), is("BAC"));
        assertThat(fiTicker.getSymbol(), is("BLK"));
        assertThat(siTicker.getSymbol(), is("BR"));
        assertThat(seTicker.getSymbol(), is("DLR"));
        assertThat(eiTicker.getSymbol(), is("FNF"));
        assertThat(niTicker.getSymbol(), is("HBI"));
        assertThat(tiTicker.getSymbol(), is("HD"));
        assertThat(fiTicker2.getSymbol(), is("IIPR"));
        assertThat(siTicker2.getSymbol(), is("INTC"));
        assertThat(seTicker2.getSymbol(), is("JPM"));
        assertThat(eiTicker2.getSymbol(), is("LEG"));
        assertThat(niTicker2.getSymbol(), is("MDT"));
        assertThat(tiTicker2.getSymbol(), is("MMM"));
        assertThat(fiTicker3.getSymbol(), is("MSM"));
        assertThat(siTicker3.getSymbol(), is("RY"));
        assertThat(seTicker3.getSymbol(), is("SBUX"));
        assertThat(eiTicker3.getSymbol(), is("SMG"));
        assertThat(niTicker3.getSymbol(), is("SPG"));
        assertThat(tiTicker3.getSymbol(), is("STOR"));
        assertThat(fiTicker4.getSymbol(), is("SWK"));
        assertThat(siTicker4.getSymbol(), is("TD"));
        assertThat(seTicker4.getSymbol(), is("TROW"));
        assertThat(eiTicker4.getSymbol(), is("TSCO"));
        assertThat(niTicker4.getSymbol(), is("TXN"));
        assertThat(tiTicker4.getSymbol(), is("UGI"));
        assertThat(fiTicker5.getSymbol(), is("V"));
        assertThat(siTicker5.getSymbol(), is("WBA"));
    }

    @Test
    void testToDayMonthYear() {
        String rawDateIntroducedExample = "August 7, 2022";
        String dateIntroduced = articlesParser.toDayMonthYear(rawDateIntroducedExample);
        assertThat(dateIntroduced, is("07.08.2022"));
    }
}
