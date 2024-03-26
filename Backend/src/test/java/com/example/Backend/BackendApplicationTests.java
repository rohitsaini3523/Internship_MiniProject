package com.example.Backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class BackendApplicationTests {

    Calculator testCalculator = new Calculator();
    @DisplayName("Addition of two number")
    @Test
    void itShouldAddTwoNumber() {
        int x = 10;
        int y = 30;
        int expectedResult = 40;
        int result = testCalculator.add(x, y);
        assertThat(result).isEqualTo(expectedResult);
    }

    @DisplayName("Check for odd number")
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 9, -7})
    void isOddShouldReturnTrue(int number) {
        boolean result = testCalculator.isOdd(number);
        assertThat(result).isTrue();
    }

    @DisplayName("Check if string is Blank")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void isStringBlankReturnTrue(String input) {
        boolean result = testCalculator.isBlank(input);
        assertThat(result).isTrue();
    }

    @DisplayName("String is not empty")
    @ParameterizedTest
    @ValueSource(strings = {"test", "rohit"})
    void isStringNotBlankReturnTrue(String input) {
        boolean result = testCalculator.isBlank(input);
        assertThat(result).isFalse();
    }

    class Calculator {
        int add(int a, int b) {
            return a + b;
        }

        boolean isOdd(int number) {
            return number % 2 != 0;
        }

        boolean isBlank(String input) {
            return input == null || input.trim().isEmpty();
        }
    }

}
