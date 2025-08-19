package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;

public class PipeData extends Component {
	
	public final boolean isTop;
	
	// Constants
	public static final int SPRITE_WIDTH = 52;
	public static final int SPRITE_HEIGHT = 320;
	public static final float SCALE = 1.25f;
	public static final float WIDTH = SPRITE_WIDTH * SCALE;
	public static final float HEIGHT = SPRITE_HEIGHT * SCALE;
	public static final float RIM_HEIGHT = 30f;
	
	public PipeData(boolean isTop) {
		this.isTop = isTop;
	}
}
