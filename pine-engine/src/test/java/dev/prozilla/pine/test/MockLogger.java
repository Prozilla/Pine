package dev.prozilla.pine.test;

import dev.prozilla.pine.common.logging.Logger;

import static org.junit.jupiter.api.Assertions.fail;

public class MockLogger extends Logger {
	
	@Override
	public void error(String message) {
		fail(message);
	}
	
}
