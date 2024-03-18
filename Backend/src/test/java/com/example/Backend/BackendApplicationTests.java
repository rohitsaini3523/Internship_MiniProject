package com.example.Backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackendApplicationTests {
	Calculator testCalculator = new Calculator();
	@Test
	void itShouldAddTwoNumber() {
		//given
		int x = 10;
		int y = 30;
		int expectedResult = 40;
		//when
		int result = testCalculator.add(x,y);

		//then
		assertThat(result).isEqualTo(expectedResult);
	}

	class Calculator {
		int add(int a, int b){
			return a+b;
		}
	}

}
