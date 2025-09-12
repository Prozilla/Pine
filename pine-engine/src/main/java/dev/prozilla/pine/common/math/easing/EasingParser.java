package dev.prozilla.pine.common.math.easing;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.common.util.SequentialParser;

public class EasingParser extends SequentialParser<EasingFunction> {
	
	@Override
	public boolean parse(String input) {
		setInput(input);
		
		Logger.system.log(input);
		
		if (isMethodCall("cubic-bezier")) {
			String arguments = readBetweenParentheses();
			Logger.system.log("Cubic bezier");
			Logger.system.log(readBetweenParentheses());
		} else if (isMethodCall("steps")) {
			Logger.system.log("Steps");
			Logger.system.log(readBetweenParentheses());
		} else {
			Easing easing = ArrayUtils.findByString(Easing.values(), input);
			if (easing == null) {
				return fail();
			} else {
				return succeed(easing);
			}
		}
		
		return fail();
	}
	
}
