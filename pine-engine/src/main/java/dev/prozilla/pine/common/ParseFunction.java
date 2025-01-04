package dev.prozilla.pine.common;

@FunctionalInterface
public interface ParseFunction<O> {
	
	O parse(String input);
	
}
