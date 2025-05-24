package dev.prozilla.pine.common.asset.pool;

public final class AssetPoolEvent {
	
	private final String path;
	private final String error;
	private final Exception exception;
	
	public AssetPoolEvent(String path) {
		this(path, null);
	}
	
	public AssetPoolEvent(String path, String error) {
		this(path, error, null);
	}
	
	public AssetPoolEvent(String path, String error, Exception exception) {
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
