package org.example.junitLearn;


import org.example.Calculator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;

/**
 * Unit test for simple App.
 */
@DisplayName("CALCULATOR TESTING")
public class CalculatorTest {
	private Calculator calculator = new Calculator();

	@BeforeAll
	public static void beforeAll() {
		System.out.println("before all");
	}

	@AfterAll
	public static void afterAll() {
		System.out.println("after all");
	}

	@BeforeEach
	public void beforeEach() {
		System.out.println("before");
	}

	@AfterEach
	public void afterEach() {
		System.out.println("after");
	}

	@Test
	@DisplayName("TEST ADD, int + int")
	public void addSuccess() {
		int result = calculator.add(2, 2);
		Assertions.assertEquals(4, result);
	}

	@Test
	@DisplayName("TEST PEMBAGIAN, int / int")
	public void divideSuccess() {
		int result = calculator.divide(4, 2);
		Assertions.assertEquals(2, result);
	}

	@Test
	@Disabled
	@DisplayName("TEST PEMBAGIAN GAGAL , int / 0")
	public void divideFail() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			calculator.divide(2, 0);
		});
	}

	@Test
	public void testAbort() {
		String profile = System.getenv("PROFILE");
		if (!"DEV".equals(profile)) {
			throw new TestAbortedException("test dibatalkan");
		}
		// unit tes untuk profile menggunakan tes aborted exception
		// setting environment variable

	}

	@Test
	public void assumtionTest() {
		Assumptions.assumeTrue("DEV".equals(System.getenv("PROFILE")));
		// test unit untuk dev menggunakan assumtion
		// setting environment variable
	}

}
