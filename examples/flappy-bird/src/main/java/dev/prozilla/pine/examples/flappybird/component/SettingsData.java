package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.NodeEvent;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.examples.flappybird.GameManager;

public class SettingsData extends Component {

	public final TextInputNode textInputNode;
	
	public SettingsData(TextInputNode textInputNode) {
		this.textInputNode = textInputNode;
		
		textInputNode.getComponent(Node.class).addListener(NodeEvent.Type.INPUT, (event) -> {
			getApplication().getLocalStorage().setItem("seed", textInputNode.getText());
			GameManager.instance.seed = Integer.parseInt(textInputNode.getText());
		});
	}
	
}
