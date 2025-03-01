package dev.prozilla.pine.common.logging.handler;

/**
 * Standard log handler that uses {@link System#err}.
 */
public class StandardErrorLogHandler implements LogHandler {
	
	@Override
	public void log() {
		System.err.println();
	}
	
	@Override
	public void log(boolean x) {
		System.err.println(x);
	}
	
	@Override
	public void log(char x) {
		System.err.println(x);
	}
	
	@Override
	public void log(int x) {
		System.err.println(x);
	}
	
	@Override
	public void log(long x) {
		System.err.println(x);
	}
	
	@Override
	public void log(float x) {
		System.err.println(x);
	}
	
	@Override
	public void log(double x) {
		System.err.println(x);
	}
	
	@Override
	public void log(char[] x) {
		System.err.println(x);
	}
	
	@Override
	public void log(Object x) {
		System.err.println(x);
	}
	
	@Override
	public void log(String text) {
		System.err.println(text);
	}
}
