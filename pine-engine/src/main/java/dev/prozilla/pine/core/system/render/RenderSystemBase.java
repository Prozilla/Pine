package dev.prozilla.pine.core.system.render;

import dev.prozilla.pine.common.lifecycle.Renderable;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.SystemBase;

import java.util.Comparator;

/**
 * Base class for systems responsible for rendering entities to the screen.
 */
public abstract class RenderSystemBase extends SystemBase implements Renderable {
	
	protected int renderPass;
	protected boolean castsShadows;
	
	@SafeVarargs
	public RenderSystemBase(Class<? extends Component>... componentTypes) {
		super(componentTypes);
		renderPass = RenderPass.DEFAULT;
		castsShadows = false;
	}
	
	protected void setRenderPass(int renderPass) {
		this.renderPass = renderPass;
	}
	
	public int getRenderPass() {
		return renderPass;
	}
	
	public void setCastsShadows(boolean castsShadows) {
		this.castsShadows = castsShadows;
	}
	
	public boolean castsShadows() {
		return castsShadows;
	}
	
	/**
	 * Renders every entity to the screen, each frame.
	 */
	@Override
	public abstract void render(Renderer renderer);
	
	@Override
	protected void beforeChunk(EntityChunk chunk) {
		Renderer renderer = application.getRenderer();
		renderer.setModelMatrix(chunk.getTransform().getModelMatrix());
	}
	
	/**
	 * Sorts the entity chunks in this render system based on their depth index.
	 */
	public void sort() {
		sort(Comparator.comparingDouble(a -> a.getTransform().position.z));
	}
}
