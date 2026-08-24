package dev.prozilla.pine.extensions.pinet.entity;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.extensions.pinet.component.NetworkManager;
import dev.prozilla.pine.extensions.pinet.packet.PacketCodec;

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
