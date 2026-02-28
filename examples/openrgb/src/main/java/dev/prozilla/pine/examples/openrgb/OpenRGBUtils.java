package dev.prozilla.pine.examples.openrgb;

import dev.prozilla.pine.common.system.Color;
import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;

public final class OpenRGBUtils {
	
	private OpenRGBUtils() {}
	
	public static OpenRGBColor convertColor(Color color) {
		return new OpenRGBColor(
			Math.round(color.getRed() * 255f),
			Math.round(color.getGreen() * 255f),
			Math.round(color.getBlue() * 255f)
		);
	}
	
}
