package org.example.junitLearn;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class RandomCalculatorTest extends AbstractCalculatorTest {

	@Test
	void tesRandom(Random random){
		int a = random.nextInt();
		int b = random.nextInt();

		int result = calculator.add(a,b);
		int expected = a+b;

		Assertions.assertEquals(expected, result);
	}
}
