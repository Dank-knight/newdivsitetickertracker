package com.bonynomo.newdivsitetickertracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class NewdivsitetickertrackerApplicationTests {

    @Test
    void contextLoads() {
        assertThat(Boolean.TRUE, is(equalTo(Boolean.TRUE)));
    }

}
