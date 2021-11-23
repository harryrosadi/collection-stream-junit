package org.example.junitLearn;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Tags({
		@Tag("integration-test")
})
public class IntegrationTest {

	@Test
	void tes1() {
	}

	@Test
	void tes2() {
	}
}
