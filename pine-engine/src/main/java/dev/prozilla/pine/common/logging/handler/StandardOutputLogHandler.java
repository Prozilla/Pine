package dev.prozilla.pine.common.logging.handler;

/**
 * Standard log handler that uses {@link System#out}.
 */
public class StandardOutputLogHandler implements LogHandler {
	
	@Override
	public void log() {
		System.out.println();
	}
	
	@Override
	public void log(boolean x) {
		System.out.println(x);
	}
	
	@Override
	public void log(char x) {
		System.out.println(x);
	}
	
	@Override
	public void log(int x) {
		System.out.println(x);
	}
	
	@Override
	public void log(long x) {
		System.out.println(x);
	}
	
	@Override
	public void log(float x) {
		System.out.println(x);
	}
	
	@Override
	public void log(double x) {
		System.out.println(x);
	}
	
	@Override
	public void log(char[] x) {
		System.out.println(x);
	}
	
	@Override
	public void log(Object x) {
		System.out.println(x);
	}
	
	@Override
	public void log(String text) {
		System.out.println(text);
	}
}
