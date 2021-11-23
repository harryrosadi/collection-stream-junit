package org.example.junitLearn;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

public class ConditionTest {

	@Test
	@EnabledOnOs({OS.WINDOWS})
	public void testWindowRun(){

	}
	@Test
	@EnabledOnOs({OS.LINUX})
	public void testLinuxRun(){

	}

	@Test
	@DisabledOnJre({JRE.JAVA_8})
	void enableJREjava8(){
	}
}
