package dev.prozilla.pine.core.system.init;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.SystemBuilder;
import dev.prozilla.pine.core.system.update.UpdateSystem;

import java.util.Objects;

/**
 * Utility class for building initialization systems.
 * @see UpdateSystem
 */
public class InitSystemBuilder extends SystemBuilder<InitSystem, InitSystemBuilder> {
	
	private Callback callback;
	
	@SafeVarargs
	public InitSystemBuilder(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	@Override
	protected InitSystemBuilder self() {
		return this;
	}
	
	public InitSystemBuilder setCallback(Callback callback) {
		this.callback = callback;
		return this;
	}
	
	@Override
	public InitSystem build() {
		Objects.requireNonNull(callback, "Callback must not be null.");
		
		InitSystem initSystem = new InitSystem(componentTypes) {
			@Override
			protected void process(EntityChunk chunk) {
				callback.run(chunk);
			}
		};
		
		return super.finishBuild(initSystem);
	}
	
	@FunctionalInterface
	public interface Callback {
		
		/**
		 * Initializes a single entity's data.
		 * @param match Entity that matches this system's query
		 */
		void run(EntityChunk chunk);
		
	}
}
