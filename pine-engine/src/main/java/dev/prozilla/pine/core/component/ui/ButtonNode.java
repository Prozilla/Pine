package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;

/**
 * A component that acts as a button in the user interface.
 */
public class ButtonNode extends Component {
	
	public boolean isHovering;
	
	public ClickCallback clickCallback;
	
	public ButtonNode() {
		isHovering = false;
	}
	
	public void click() {
		if (clickCallback == null) {
			return;
		}
		
		clickCallback.click(entity);
	}
	
	@Override
	public String getName() {
		return "ButtonData";
	}
	
	@FunctionalInterface
	public interface ClickCallback {
		
		/**
		 * Triggered whenever the button is clicked.
		 * @param entity Button entity
		 */
		void click(Entity entity);
		
	}
}
