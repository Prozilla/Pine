package dev.prozilla.pine.common;

import dev.prozilla.pine.common.util.Parser;

/**
 * @deprecated Replaced by {@link Parser} as of 2.0.2
 */
@Deprecated
@FunctionalInterface
public interface ParseFunction<O> {
	
	O parse(String input);
	
}
