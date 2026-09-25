package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.event.EventDispatcher;

public class AssetPoolEventDispatcher<T extends Asset> extends EventDispatcher<AssetPoolEvent.Type, AssetPool<T>, AssetPoolEvent<T>> {
	
	public boolean invoke(AssetPoolEvent.Type type, AssetPool<T> target, String path) {
		return invoke(type, target, path, null, null);
	}
	
	public boolean invoke(AssetPoolEvent.Type type, AssetPool<T> target, String path, String error, Exception exception) {
		return invoke(new AssetPoolEvent<>(type, target, path, error, exception));
	}
	
	@Override
	protected AssetPoolEvent<T> createEvent(AssetPoolEvent.Type type, AssetPool<T> target) {
		return new AssetPoolEvent<>(type, target, null);
	}
	
}
