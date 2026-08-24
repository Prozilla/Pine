package dev.prozilla.pine.extensions.pinet.system;

import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.extensions.pinet.component.NetworkManager;

public class NetworkSynchronizer extends UpdateSystem {
	
	public NetworkSynchronizer() {
		super(NetworkManager.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		NetworkManager network = chunk.getComponent(NetworkManager.class);
		network.synchronize();
	}
	
}
