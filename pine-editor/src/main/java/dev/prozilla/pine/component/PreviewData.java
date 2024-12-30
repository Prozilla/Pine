package dev.prozilla.pine.component;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.Component;

public class PreviewData extends Component {
	
	public Application preview;
	
	public PreviewData(Application preview) {
		this.preview = preview;
	}
}
