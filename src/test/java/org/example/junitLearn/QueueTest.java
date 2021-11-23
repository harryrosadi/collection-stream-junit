package org.example.junitLearn;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

@DisplayName("antrian test")
public class QueueTest {

	private Queue<String> queue;

	/**
	 * @Nested class dalam class
	 * bisa digunakan untuk test beberapa class
	 * jika banyak yang harus di test
	 **/

	@Nested
	@DisplayName("when new")
	public class WhenNew {
		@BeforeEach
		void setUp() {
			queue = new LinkedList<>();
		}

		@Test
		@DisplayName("when offer, size must be 1")
		void addOfferNewData() {
			queue.offer("halo");
			Assertions.assertEquals(1, queue.size());
		}

		@Test
		@DisplayName("when offer, size must be 2")
		void offerMoreData() {
			queue.offer("halo");
			queue.offer("dunia");
			Assertions.assertEquals(2, queue.size());

		}
	}
}
