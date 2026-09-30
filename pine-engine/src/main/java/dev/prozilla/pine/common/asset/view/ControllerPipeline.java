package dev.prozilla.pine.common.asset.view;

import dev.prozilla.pine.common.util.collection.Pipeline;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.Collection;

public class ControllerPipeline extends Pipeline<Controller> implements Controller {
	
	public ControllerPipeline() {}
	
	public ControllerPipeline(Controller... elements) {
		super(elements);
	}
	
	public ControllerPipeline(Collection<? extends Controller> elements) {
		super(elements);
	}
	
	@Override
	public void load(Node view) {
		for (Controller controller : this) {
			controller.load(view);
		}
	}
	
	@Override
	public void unload(Node view) {
		for (Controller controller : this) {
			controller.unload(view);
		}
	}
	
	/**
	 * Adds the given controller to this pipeline
	 * @param controller The controller to add to this pipeline.
	 * @return This pipeline.
	 */
	@Override
	public ControllerPipeline then(Controller controller) {
		add(controller);
		return this;
	}
	
}
