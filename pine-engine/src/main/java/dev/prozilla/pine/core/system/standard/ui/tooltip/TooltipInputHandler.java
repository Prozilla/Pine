package dev.prozilla.pine.core.system.standard.ui.tooltip;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TooltipNode;
import dev.prozilla.pine.core.system.input.InputSystemBase;

public class TooltipInputHandler extends InputSystemBase {
	
	public TooltipInputHandler() {
		super(TooltipNode.class, Node.class);
	}
	
	@Override
	public void input(float deltaTime) {
		Vector2i cursorPosition = getInput().getCursor(true);
		
		int windowWidth = application.getWindow().width;
		int windowHeight = application.getWindow().height;
		
		forEach((chunk) -> {
			Node node = chunk.getComponent(Node.class);
			TooltipNode tooltipNode = chunk.getComponent(TooltipNode.class);
			
			tooltipNode.cursorX.setValue(Math.round(cursorPosition.x * (1f - (node.anchor.x * 2))));
			tooltipNode.cursorY.setValue(Math.round(cursorPosition.y * ((node.anchor.y * 2) - 1f)));
			tooltipNode.baseX.setValue(Math.round(windowWidth * (node.anchor.x)));
			tooltipNode.baseY.setValue(Math.round(windowHeight * (1f - node.anchor.y)));
		});
	}
}
