package org.example.junitLearn;

import org.junit.jupiter.api.Test;

public class LifeCycleTest {
	/**
	 * testing menggunakan j unit ini tidak berketergantungan dengan objek
	 * lainnya, karena dia independent
	 * j unit ini sendiri secara default dia menjalankan test secara random
	 * atau tidak berurutan
	 * jika ingin berurut bisa menggunakan anotasi @TestMethodOrder
	 **/

	String temp;

	@Test
	void tesB() {
		temp= "halo";
	}

	@Test
	void tesC() {
		System.out.println(temp);

	}
}
