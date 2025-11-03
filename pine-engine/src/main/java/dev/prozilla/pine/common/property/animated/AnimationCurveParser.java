package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.math.easing.EasingParser;
import dev.prozilla.pine.common.util.parser.Parser;

public class AnimationCurveParser extends Parser<AnimationCurve> {
	
	private static final EasingParser easingParser = new EasingParser();
	
	public boolean parse(String input) {
		String[] parts = input.split(" ");
		
		float duration;
		try {
			if (parts[0].endsWith("ms")) {
				duration = Float.parseFloat(parts[0].substring(0, parts[0].length() - 2)) / 1000f;
			} else if (parts[0].endsWith("s")) {
				duration = Float.parseFloat(parts[0].substring(0, parts[0].length() - 1));
			} else {
				return fail();
			}
		} catch (NumberFormatException e) {
			return fail("Invalid duration value");
		}
		
		if (parts.length == 1) {
			return succeed(new AnimationCurve(duration));
		}
		
		easingParser.parse(parts[1]);
		EasingFunction easingFunction = easingParser.getResult();
		if (easingFunction == null) {
			return fail("Invalid easing function name");
		}
		if (parts.length == 2) {
			return succeed(new AnimationCurve(duration, easingFunction));
		}
		
		AnimationDirection direction = AnimationDirection.parse(input);
		if (direction == null) {
			return fail("Invalid direction name");
		}
		return succeed(new AnimationCurve(duration, easingFunction, direction));
	}
	
}
