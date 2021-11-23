package org.example.junitLearn;


import org.example.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;

@Extensions({
		@ExtendWith(RandomParameterResolver.class)
})
public class AbstractCalculatorTest {
	protected Calculator calculator = new Calculator();

	@BeforeEach
	void setUp() {
		System.out.println("before each turun dari abstract calculator");
	}
}
