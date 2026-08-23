package dev.prozilla.pine.examples.sokoban.net.entity;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.examples.sokoban.net.component.NetworkManager;
import dev.prozilla.pine.examples.sokoban.net.packet.PacketCodec;

public class NetworkManagerPrefab extends Prefab {
	
	protected PacketCodec codec;
	
	public void setCodec(PacketCodec codec) {
		this.codec = codec;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(codec != null ? new NetworkManager(codec) : new NetworkManager());
	}
}
