package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for constructing <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">ANSI escape sequences</a>.
 */
public final class Ansi {
	
	public static final String RESET = "\u001B[0m";
	
	// Foreground colors
	public static final String BLACK = "\u001B[30m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String PURPLE = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";
	
	// Background colors
	public static final String BLACK_BACKGROUND = "\u001B[40m";
	public static final String RED_BACKGROUND = "\u001B[41m";
	public static final String GREEN_BACKGROUND = "\u001B[42m";
	public static final String YELLOW_BACKGROUND = "\u001B[43m";
	public static final String BLUE_BACKGROUND = "\u001B[44m";
	public static final String PURPLE_BACKGROUND = "\u001B[45m";
	public static final String CYAN_BACKGROUND = "\u001B[46m";
	public static final String WHITE_BACKGROUND = "\u001B[47m";
	
	// Decorations
	public static final String BOLD = "\u001b[1m";
	public static final String DIM = "\u001b[2m";
	public static final String ITALIC = "\u001b[3m";
	public static final String UNDERLINED = "\u001b[4m";
	
	private Ansi() {}
	
	public static @NotNull String reset(String text) {
		return RESET + text;
	}
	
	public static @NotNull String black(String text) {
		return color(text, BLACK);
	}
	
	public static @NotNull String red(String text) {
		return color(text, RED);
	}
	
	public static @NotNull String green(String text) {
		return color(text, GREEN);
	}
	
	public static @NotNull String yellow(String text) {
		return color(text, YELLOW);
	}
	
	public static @NotNull String blue(String text) {
		return color(text, BLUE);
	}
	
	public static @NotNull String purple(String text) {
		return color(text, PURPLE);
	}
	
	public static @NotNull String cyan(String text) {
		return color(text, CYAN);
	}
	
	public static @NotNull String white(String text) {
		return color(text, WHITE);
	}
	
	/**
	 * Sets the color of the given text.
	 */
	@Contract("null, _ -> fail; _, null -> fail; !null, !null -> !null")
	public static String color(String text, String ansiColor) {
		Checks.isNotNull(text, "text");
		Checks.isNotNull(ansiColor, "ansiColor");
		
		String result = ansiColor + text;
		if (!result.endsWith(RESET)) {
			result += RESET;
		}
		return result;
	}
	
	public static @NotNull String blackBg(String text) {
		return colorBg(text, BLACK_BACKGROUND);
	}
	
	public static @NotNull String redBg(String text) {
		return colorBg(text, RED_BACKGROUND);
	}
	
	public static @NotNull String greenBg(String text) {
		return colorBg(text, GREEN_BACKGROUND);
	}
	
	public static @NotNull String yellowBg(String text) {
		return colorBg(text, YELLOW_BACKGROUND);
	}
	
	public static @NotNull String blueBg(String text) {
		return colorBg(text, BLUE_BACKGROUND);
	}
	
	public static @NotNull String purpleBg(String text) {
		return colorBg(text, PURPLE_BACKGROUND);
	}
	
	public static @NotNull String cyanBg(String text) {
		return colorBg(text, CYAN_BACKGROUND);
	}
	
	public static @NotNull String whiteBg(String text) {
		return colorBg(text, WHITE_BACKGROUND);
	}
	
	/**
	 * Sets the background color of the given text.
	 */
	@Contract("null, _ -> fail; _, null -> fail; !null, !null -> !null")
	public static String colorBg(String text, String ansiColorBg) {
		Checks.isNotNull(text, "text");
		Checks.isNotNull(ansiColorBg, "ansiColorBg");
		
		String result = ansiColorBg + text;
		if (!result.endsWith(RESET)) {
			result += RESET;
		}
		return result;
	}
	
	public static @NotNull String bold(String text) {
		return decorate(text, BOLD);
	}
	
	public static @NotNull String dim(String text) {
		return decorate(text, DIM);
	}
	
	public static @NotNull String italic(String text) {
		return decorate(text, ITALIC);
	}
	
	public static @NotNull String underline(String text) {
		return decorate(text, UNDERLINED);
	}
	
	/**
	 * Sets the decoration of the given text.
	 */
	@Contract("null, _ -> fail; _, null -> fail; !null, !null -> !null")
	public static String decorate(String text, String ansiDecoration) {
		Checks.isNotNull(text, "text");
		Checks.isNotNull(ansiDecoration, "ansiDecoration");
		
		String result = ansiDecoration + text;
		if (!result.endsWith(RESET)) {
			result += RESET;
		}
		return result;
	}
	
	/**
	 * Removes all ANSI escape codes from a given text.
	 */
	@Contract("null -> fail; !null -> !null")
	public static String strip(String text) {
		Checks.isNotNull(text, "text");
		return text.replaceAll("\u001B\\[[;\\d]*m", "");
	}
	
	public static Color toColor(String ansiColor) {
		return switch (ansiColor) {
			case BLACK, BLACK_BACKGROUND -> Color.black();
			case RED, RED_BACKGROUND -> Color.red();
			case GREEN, GREEN_BACKGROUND -> Color.green();
			case YELLOW, YELLOW_BACKGROUND -> Color.yellow();
			case BLUE, BLUE_BACKGROUND -> Color.blue();
			case PURPLE, PURPLE_BACKGROUND -> Color.purple();
			case CYAN, CYAN_BACKGROUND -> Color.cyan();
			case WHITE, WHITE_BACKGROUND -> Color.white();
			default -> null;
		};
	}
	
}
