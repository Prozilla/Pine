package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.core.component.Component;

public class BarData extends Component {
	
	public final int index;
	public final BarRect barRect;
	
	public BarData(int index, BarRect barRect) {
		this.index = index;
		this.barRect = barRect;
	}
	
}
