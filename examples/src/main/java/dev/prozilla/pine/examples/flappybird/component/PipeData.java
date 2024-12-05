package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;

public class PipeData extends Component {
	
	public final boolean isTop;
	
	// Constants
	public static final int WIDTH = 52;
	public static final int HEIGHT = 320;
	
	public PipeData(boolean isTop) {
		this.isTop = isTop;
	}
}
