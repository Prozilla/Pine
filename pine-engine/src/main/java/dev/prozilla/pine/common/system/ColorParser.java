package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.common.util.StringUtils;

public class ColorParser extends Parser<Color> {
	
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
			return fail("Invalid input format");
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
