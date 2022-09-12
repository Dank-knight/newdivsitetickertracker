package com.bonynomo.newdivsitetickertracker.service;

import com.bonynomo.newdivsitetickertracker.client.W3mClient;
import com.bonynomo.newdivsitetickertracker.model.Ticker;
import com.bonynomo.newdivsitetickertracker.parse.ArticlesParser;
import com.bonynomo.newdivsitetickertracker.repo.TickerRepo;
import com.bonynomo.newdivsitetickertracker.util.SelfMadeJitter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private TickerRepo tickerRepo;
    @Mock
    private ArticlesParser articlesParser;
    @Mock
    private W3mClient w3mClient;
    @Mock
    private SelfMadeJitter jitter;

    @InjectMocks
    private ArticleService articleService;

    @Test
    void when_InitMethodIsExecutedWithUpdateTypeOfTickers_Expect_SaveSetOfRelevantTickersNotExecuted() {
        ReflectionTestUtils.setField(articleService, "url",
                "https://dividendstockpile.com/");
        when(w3mClient.getW3mOutputByUrl(anyString())).thenReturn("test");
        when(articlesParser.extractUndervaluedDividendGrowthStocksArticleTitles(anyString()))
                .thenReturn(List.of("10 Undervalued Dividend Growth Stocks To Research The Week of 02/14/2022"));
        Ticker aapl = Ticker.builder()
                .symbol("AAPL")
                .dateIntroduced("14.02.2022")
                .isActive(false)
                .build();
        Map<String, List<Ticker>> newUndervaluedTickers = Map.of(
                "Update", List.of(aapl),
                "Save", Collections.emptyList(),
                "Set", Collections.emptyList());
        when(articlesParser.extractTickersFromPage(anyString())).thenReturn(newUndervaluedTickers);

        articleService.init();

        verify(tickerRepo, times(1)).findBySymbol("AAPL");
    }
}
