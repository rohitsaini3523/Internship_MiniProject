package com.example.Backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class BackendApplicationTests {

    Calculator testCalculator = new Calculator();

    @Test
    void itShouldAddTwoNumber() {
        int x = 10;
        int y = 30;
        int expectedResult = 40;
        int result = testCalculator.add(x, y);
        assertThat(result).isEqualTo(expectedResult);
    }

    class Calculator {
        int add(int a, int b) {
            return a + b;
        }
    }

}
