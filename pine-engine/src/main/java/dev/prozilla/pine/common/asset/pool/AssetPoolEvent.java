package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.event.Event;

public final class AssetPoolEvent<T extends Asset> extends Event<AssetPoolEventType, AssetPool<T>> {
	
	private final String path;
	private final String error;
	private final Exception exception;
	
	public AssetPoolEvent(AssetPoolEventType type, AssetPool<T> target, String path) {
		this(type, target, path, null);
	}
	
	public AssetPoolEvent(AssetPoolEventType type, AssetPool<T> target, String path, String error) {
		this(type, target, path, error, null);
	}
	
	public AssetPoolEvent(AssetPoolEventType type, AssetPool<T> target, String path, String error, Exception exception) {
		super(type, target);
		this.path = path;
		this.error = error;
		this.exception = exception;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getError() {
		return error;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public boolean hasFailed() {
		return error != null || exception != null;
	}
	
}
