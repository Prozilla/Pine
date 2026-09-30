package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.asset.view.Controller;
import dev.prozilla.pine.common.asset.view.View;
import dev.prozilla.pine.core.component.Component;

public class ViewNode extends Component {
	
	public final View view;
	public Controller controller;
	
	public ViewNode(View view, Controller controller) {
		this.view = view;
		this.controller = controller;
	}
	
}
