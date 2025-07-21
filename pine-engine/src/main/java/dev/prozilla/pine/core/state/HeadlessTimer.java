package dev.prozilla.pine.core.state;

public class HeadlessTimer extends Timer {
	
	@Override
	public double getCurrentTime() {
		return System.nanoTime();
	}
}
