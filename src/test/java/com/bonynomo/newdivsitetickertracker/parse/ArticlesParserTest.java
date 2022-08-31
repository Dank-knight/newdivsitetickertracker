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
    void testFirstSampleConvertToTickers() {
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
    void testSecondSampleConvertToTickers() {
        String pageAsString = "The following are new companies on the list this week: \n" +
                "\n" +
                "• INTC\n" +
                "• LEG\n" +
                "• TXN\n" +
                "\n" +
                "The others – FNF, HBI, IIPR, MDT, MMM, SBUX, SWK, and TROW continue from last\n" +
                "week as remaining undervalued.\n" +
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
        assertThat(fTicker.getSymbol(), is("INTC"));
        assertThat(sTicker.getSymbol(), is("LEG"));
        assertThat(tTicker.getSymbol(), is("TXN"));
    }

    @Test
    void testThirdSampleConvertToTickers() {
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
    void testFourthSampleConvertToTickers() {
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
    void testFifthSampleConvertToTickers() {

        String pageAsString = "The new companies to make the list this week are:\n" +
                "\n" +
                "APD, BAC, INTC, MCD, SYY, WBA\n" +
                "\n" +
                "The remaining names – AVGO, BLK, BR, CMI, FNF, HBI, IIPR, IP, JPM, LEG, MDT,\n" +
                "MMM, MSM, PRU, SBUX, SWK, TROW, TSCO, TXN, UGI and V are a continuation of last\n" +
                "week and remain undervalued.\n" +
                "\n" +
                "This list should be used to begin your research to determine if the stock meets\n" +
                "all of your investment goals and criteria. Valuation should only be one of many\n" +
                "aspects you look at when deciding to make an investment.";
        String partBetween = articlesParser.getPartBetween("The new companies to make the list this week are:", "The remaining names – ", pageAsString);
        List<Ticker> tickers = articlesParser.convertDirtyTickersToListOfTickers(DATE_INTRODUCED, partBetween, true);
        assertThat(tickers, hasSize(6));
        Ticker fTicker = tickers.get(0);
        Ticker sTicker = tickers.get(1);
        Ticker tTicker = tickers.get(2);
        Ticker foTicker = tickers.get(3);
        Ticker fiTicker = tickers.get(4);
        Ticker siTicker = tickers.get(5);
        assertThat(fTicker.getSymbol(), is("APD"));
        assertThat(sTicker.getSymbol(), is("BAC"));
        assertThat(tTicker.getSymbol(), is("INTC"));
        assertThat(foTicker.getSymbol(), is("MCD"));
        assertThat(fiTicker.getSymbol(), is("SYY"));
        assertThat(siTicker.getSymbol(), is("WBA"));
    }

    @Test
    void testSixthSampleConvertToTickers() {

        String pageAsString = "There are no new companies to make the list this week, but the following have\n" +
                "been removed since the last analysis:\n" +
                "\n" +
                "INTC, MCD, SYY, AVGO, BLK, IP, MSM, PRU, TSCO\n" +
                "\n" +
                "This list should be used to begin your research to determine if the stock meets\n" +
                "all of your investment goals and criteria. Valuation should only be one of many\n" +
                "aspects you look at when deciding to make an investment.";
        String partBetween = articlesParser.getPartBetween("There are no new companies to make the list this week, but the following have\n" + "been removed since the last analysis:",
                "This list should be used to begin your research to determine if the stock meets", pageAsString);
        List<Ticker> tickers = articlesParser.convertDirtyTickersToListOfTickers(DATE_INTRODUCED, partBetween, true);
        assertThat(tickers, hasSize(9));
        Ticker fTicker = tickers.get(0);
        Ticker sTicker = tickers.get(1);
        Ticker tTicker = tickers.get(2);
        Ticker foTicker = tickers.get(3);
        Ticker fiTicker = tickers.get(4);
        Ticker siTicker = tickers.get(5);
        Ticker seTicker = tickers.get(6);
        Ticker eiTicker = tickers.get(7);
        Ticker niTicker = tickers.get(8);
        assertThat(fTicker.getSymbol(), is("INTC"));
        assertThat(sTicker.getSymbol(), is("MCD"));
        assertThat(tTicker.getSymbol(), is("SYY"));
        assertThat(foTicker.getSymbol(), is("AVGO"));
        assertThat(fiTicker.getSymbol(), is("BLK"));
        assertThat(siTicker.getSymbol(), is("IP"));
        assertThat(seTicker.getSymbol(), is("MSM"));
        assertThat(eiTicker.getSymbol(), is("PRU"));
        assertThat(niTicker.getSymbol(), is("TSCO"));
    }

    @Test
    void testSeventhSampleConvertToTickers() {
        String pageAsString = "New on the list this week: DLR, HD, IIPR, MSM, and TSCO\n" +
                "\n" +
                "APD, BAC, BR, CMI, FNF, HBI, JPM, LEG, MDT, MMM, SBUX, SWK, TROW, UGI, V, and\n" +
                "WBA continue from last week to look undervalued.\n" +
                "\n" +
                "This list should be used to begin your research to determine if the stock meets\n" +
                "all of your investment goals and criteria. Valuation should only be one of many\n" +
                "aspects you look at when deciding to make an investment.";
        String partBetween = articlesParser.getPartBetween("New on the list this week:", "APD, BAC, BR, CMI, FNF, HBI, JPM, LEG, MDT, MMM, SBUX, SWK, TROW, UGI, V, and\n" + "WBA continue from last week to look undervalued.", pageAsString);
        List<Ticker> tickers = articlesParser.convertDirtyTickersToListOfTickers(DATE_INTRODUCED, partBetween, true);
        assertThat(tickers, hasSize(5));
        Ticker fTicker = tickers.get(0);
        Ticker sTicker = tickers.get(1);
        Ticker tTicker = tickers.get(2);
        Ticker foTicker = tickers.get(3);
        Ticker fiTicker = tickers.get(4);
        assertThat(fTicker.getSymbol(), is("DLR"));
        assertThat(sTicker.getSymbol(), is("HD"));
        assertThat(tTicker.getSymbol(), is("IIPR"));
        assertThat(foTicker.getSymbol(), is("MSM"));
        assertThat(fiTicker.getSymbol(), is("TSCO"));
    }

    @Test
    void testEighthSampleConvertToTickers() {

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
    void testNinthSampleConvertToTickers() {
        String pageAsString = "New on the list this week: AOS, BLK, INTC, SMG, TD, TXN\n" +
                "\n" +
                "APD, BAC, BR, CMI, FNF, HBI, HD, IIPR, JPM, LEG, MDT, MMM, MSM, SBUX, SWK,\n" +
                "TROW, TSCO, UGI, V, and WBA continue from last week to look undervalued.\n" +
                "\n" +
                "This list should be used to begin your research to determine if the stock meets\n" +
                "all of your investment goals and criteria. Valuation should only be one of many\n" +
                "aspects you look at when deciding to make an investment.";
        String partBetween = articlesParser.getPartBetween("New on the list this week:", "APD, BAC, BR, CMI,", pageAsString);
        List<Ticker> tickers = articlesParser.convertDirtyTickersToListOfTickers(DATE_INTRODUCED, partBetween, true);
        assertThat(tickers, hasSize(6));
        Ticker fTicker = tickers.get(0);
        Ticker sTicker = tickers.get(1);
        Ticker tTicker = tickers.get(2);
        Ticker foTicker = tickers.get(3);
        Ticker fiTicker = tickers.get(4);
        Ticker siTicker = tickers.get(5);

        assertThat(fTicker.getSymbol(), is("AOS"));
        assertThat(sTicker.getSymbol(), is("BLK"));
        assertThat(tTicker.getSymbol(), is("INTC"));
        assertThat(foTicker.getSymbol(), is("SMG"));
        assertThat(fiTicker.getSymbol(), is("TD"));
        assertThat(siTicker.getSymbol(), is("TXN"));
    }

    @Test
    void testTenthSampleConvertToTickers() {
        String pageAsString = "Stocks Listed: AOS, APD, BAC, BLK, BR, CMI, DLR, FNF, HBI, HD, IIPR, JPM, LEG,\n" +
                "LOW, MDT, MMM, MS, MSM, PRU, SMG, SWK, TD, V\n" +
                "\n" +
                "This list should be used to begin your research to determine if the stock meets\n" +
                "all of your investment goals and criteria. Valuation should only be one of many\n" +
                "aspects you look at when deciding to make an investment.";


        String partBetween = articlesParser.getPartBetween("Stocks Listed:", "This list should be used to begin your research to determine if the stock meets", pageAsString);
        List<Ticker> tickers = articlesParser.convertDirtyTickersToListOfTickers(DATE_INTRODUCED, partBetween, true);
        assertThat(tickers, hasSize(23));
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

        assertThat(fTicker.getSymbol(), is("AOS"));
        assertThat(sTicker.getSymbol(), is("APD"));
        assertThat(tTicker.getSymbol(), is("BAC"));
        assertThat(foTicker.getSymbol(), is("BLK"));
        assertThat(fiTicker.getSymbol(), is("BR"));
        assertThat(siTicker.getSymbol(), is("CMI"));
        assertThat(seTicker.getSymbol(), is("DLR"));
        assertThat(eiTicker.getSymbol(), is("FNF"));
        assertThat(niTicker.getSymbol(), is("HBI"));
        assertThat(tiTicker.getSymbol(), is("HD"));
        assertThat(fiTicker2.getSymbol(), is("IIPR"));
        assertThat(siTicker2.getSymbol(), is("JPM"));
        assertThat(seTicker2.getSymbol(), is("LEG"));
        assertThat(eiTicker2.getSymbol(), is("LOW"));
        assertThat(niTicker2.getSymbol(), is("MDT"));
        assertThat(tiTicker2.getSymbol(), is("MMM"));
        assertThat(fiTicker3.getSymbol(), is("MS"));
        assertThat(siTicker3.getSymbol(), is("MSM"));
        assertThat(seTicker3.getSymbol(), is("PRU"));
        assertThat(eiTicker3.getSymbol(), is("SMG"));
        assertThat(niTicker3.getSymbol(), is("SWK"));
        assertThat(tiTicker3.getSymbol(), is("TD"));
        assertThat(fiTicker4.getSymbol(), is("V"));
    }

    @Test
    void testEleventhSampleConvertToTickers() {
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
}