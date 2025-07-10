package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.event.EventDispatcher;

public class AssetPoolEventDispatcher<T extends Asset> extends EventDispatcher<AssetPoolEventType, AssetPool<T>, AssetPoolEvent<T>> {
	
	public void invoke(AssetPoolEventType type, AssetPool<T> target, String path) {
		invoke(type, target, path, null, null);
	}
	
	public void invoke(AssetPoolEventType type, AssetPool<T> target, String path, String error, Exception exception) {
		invoke(new AssetPoolEvent<>(type, target, path, error, exception));
	}
	
	@Override
	protected AssetPoolEvent<T> createEvent(AssetPoolEventType type, AssetPool<T> target) {
		return new AssetPoolEvent<>(type, target, null);
	}
	
}
