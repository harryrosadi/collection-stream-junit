package org.example.junitLearn;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;



//@TestMethodOrder(MethodOrderer.Random.class) // jalan secara random
//@TestMethodOrder(MethodOrderer.DisplayName.class) // jalan bedarsarkan display name
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // jalan bedasarkan order
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
/** @TestInstance PER_CLASS mempermudah untuk membuat satu objek yang bisa dipakai disemua method dan jadi
tidak independent  **/
public class OrderTest {

	@Test
	@Order(2)
	void tes1() {
	}

	@Test
	@Order(1)
	void tes2() {
	}

	@Test
	@Order(3)
	void tes3() {
	}
}
