package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ColorParser extends Parser<Color> {
	
	private final Map<String, Supplier<Color>> colorNames;
	
	public ColorParser() {
		colorNames = new HashMap<>();
		colorNames.put("white", Color::white);
		colorNames.put("black", Color::black);
		colorNames.put("red", Color::red);
		colorNames.put("green", Color::green);
		colorNames.put("blue", Color::blue);
		colorNames.put("transparent", Color::transparent);
		colorNames.put("aqua", Color::aqua);
		colorNames.put("cyan", Color::cyan);
		colorNames.put("darkblue", Color::darkBlue);
		colorNames.put("darkcyan", Color::darkCyan);
		colorNames.put("darkgray", Color::darkGray);
		colorNames.put("darkgrey", Colour::darkGrey);
		colorNames.put("darkgreen", Color::darkGreen);
		colorNames.put("darkmagenta", Color::darkMagenta);
		colorNames.put("darkorange", Color::darkOrange);
		colorNames.put("darkred", Color::darkRed);
		colorNames.put("dimgray", Color::dimGray);
		colorNames.put("dimgrey", Colour::dimGrey);
		colorNames.put("gold", Color::gold);
		colorNames.put("gray", Color::gray);
		colorNames.put("grey", Colour::grey);
		colorNames.put("lightblue", Color::lightBlue);
		colorNames.put("lightcyan", Color::lightCyan);
		colorNames.put("lightgray", Color::lightGray);
		colorNames.put("lightgrey", Colour::lightGrey);
		colorNames.put("lightgreen", Color::lightGreen);
		colorNames.put("lightyellow", Color::lightYellow);
		colorNames.put("lime", Color::lime);
		colorNames.put("magenta", Color::magenta);
		colorNames.put("maroon", Color::maroon);
		colorNames.put("mediumblue", Color::mediumBlue);
		colorNames.put("mintcream", Color::mintCream);
		colorNames.put("navy", Color::navy);
		colorNames.put("olive", Color::olive);
		colorNames.put("orange", Color::orange);
		colorNames.put("orangered", Color::orangeRed);
		colorNames.put("pink", Color::pink);
		colorNames.put("purple", Color::purple);
		colorNames.put("rebeccapurple", Color::rebeccaPurple);
		colorNames.put("silver", Color::silver);
		colorNames.put("skyblue", Color::skyBlue);
		colorNames.put("springgreen", Color::springGreen);
		colorNames.put("teal", Color::teal);
		colorNames.put("tomato", Color::tomato);
		colorNames.put("turquoise", Color::turquoise);
		colorNames.put("violet", Color::violet);
		colorNames.put("whitesmoke", Color::whiteSmoke);
		colorNames.put("yellow", Color::yellow);
	}
	
	@Override
	public boolean parse(String input) {
		int start;
		boolean includesAlpha = false;
		
		if (input.startsWith("#")) {
			try {
				return succeed(Color.decode(input));
			} catch (NumberFormatException e) {
				return fail("Invalid HEX value");
			}
		} else if (input.startsWith("rgba(")) {
			start = 4;
			includesAlpha = true;
		} else if (input.startsWith("rgb(")) {
			start = 3;
		} else {
			Supplier<Color> factory = colorNames.get(input.toLowerCase());
			if (factory == null) {
				return fail("Invalid input");
			}
			return succeed(factory.get());
		}
		
		int end = StringUtils.findClosingParenthesis(input, start - 1);
		if (end == -1) {
			return fail("Missing closing bracket");
		}
		
		String inner = input.substring(start, end + 1);
		
		if (includesAlpha) {
			Vector4f vector4f = Vector4f.parse(inner);
			return succeed(new Color(vector4f.x, vector4f.y, vector4f.z, vector4f.w));
		} else {
			Vector3f vector3f = Vector3f.parse(inner);
			return succeed(new Color(vector3f.x, vector3f.y, vector3f.z));
		}
	}
	
}
