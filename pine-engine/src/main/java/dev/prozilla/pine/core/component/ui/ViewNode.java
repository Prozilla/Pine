package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.prefab.ui.Controller;
import dev.prozilla.pine.core.entity.prefab.ui.View;

public class ViewNode extends Component {
	
	public final View view;
	public Controller controller;
	
	public ViewNode(View view, Controller controller) {
		this.view = view;
		this.controller = controller;
	}
	
}
