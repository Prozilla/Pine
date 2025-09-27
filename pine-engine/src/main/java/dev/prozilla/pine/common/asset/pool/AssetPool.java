package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.system.PathUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for pools of assets.
 * Pools manage asset loading efficiently by avoiding loading assets multiple times.
 * @param <T> The type of assets in this pool
 */
public abstract class AssetPool<T extends Asset> implements Destructible {
	
	private final Map<String, T> pool;
	protected final AssetPoolEventDispatcher<T> eventDispatcher;
	
	public static final String UNKNOWN_ERROR = "Unknown error";
	public static final String NOT_FOUND_ERROR = "File not found";
	
	public AssetPool() {
		pool = new HashMap<>();
		eventDispatcher = new AssetPoolEventDispatcher<>();
	}
	
	/**
	 * Loads an asset from a path or retrieves it from the pool if it has already been loaded once.
	 *
	 * <p>This method is protected because some subclasses may require additional parameters to load an asset.</p>
	 * @param path The path of the asset
	 * @return The asset, or {@code null} if it failed to load.
	 */
	protected T load(String path) {
		Checks.isNotNull(path, "path");
		path = normalize(path);
		String key = createKey(path);
		
		if (pool.containsKey(key)) {
			prepareNext();
			return pool.get(key);
		}
		
		eventDispatcher.invoke(AssetPoolEventType.LOADING, this, path);
		
		// Create asset
		T asset = null;
		try {
			asset = createAsset(path);
		} catch (Exception ignored) {}
		
		// Verify asset
		if (asset == null) {
			return null;
		}
		
		eventDispatcher.invoke(AssetPoolEventType.LOADED, this, path);
		pool.put(key, asset);
		prepareNext();
		return asset;
	}
	
	/**
	 * Prepares this pool for the next asset.
	 */
	protected void prepareNext() {
	
	}
	
	/**
	 * Creates a new asset from a normalized path.
	 * @param path The normalized path
	 * @return The new asset, or {@code null} if the asset creation failed.
	 */
	protected abstract T createAsset(String path);
	
	/**
	 * Utility method for marking the asset creation as failed.
	 * @param reason The reason of the failure
	 * @return {@code null}
	 */
	protected T fail(String path, String reason) {
		return fail(path, reason, null);
	}
	
	/**
	 * Utility method for marking the asset creation as failed.
	 * @param reason The reason of the failure
	 * @return {@code null}
	 */
	@Contract("_, _, _ -> null")
	protected T fail(String path, String reason, Exception exception) {
		eventDispatcher.invoke(AssetPoolEventType.FAILED, this, path, reason, exception);
		return null;
	}
	
	/**
	 * Returns the assets stored in this pool.
	 * @return The assets stored in this pool.
	 */
	protected final Collection<T> getAssets() {
		return pool.values();
	}
	
	/**
	 * Removes an asset from this pool.
	 * @param asset The asset to remove
	 * @return {@code false} if {@code path} does not match any asset in this pool.
	 */
	public boolean remove(T asset) {
		return remove(asset.getPath());
	}
	
	/**
	 * Removes an asset from this pool.
	 * @param path The path of the asset to remove
	 * @return {@code false} if {@code path} does not match any asset in this pool.
	 */
	protected boolean remove(String path) {
		if (path == null) {
			return false;
		}
		String key = createKey(normalize(path));
		boolean removed = pool.remove(key) != null;
		if (removed) {
			eventDispatcher.invoke(AssetPoolEventType.REMOVED, this, path);
		}
		return removed;
	}
	
	/**
	 * Destroys all assets in this pool and removes them.
	 */
	@Override
	public void destroy() {
		Destructible.destroyAndClear(pool.values());
	}
	
	/**
	 * Returns the number of assets in this pool.
	 * @return The number of assets in this pool.
	 */
	public int count() {
		return pool.size();
	}
	
	/**
	 * Creates a key for the current asset.
	 * @param path The normalized path of the asset
	 * @return The new key.
	 */
	protected String createKey(String path) {
		return path;
	}
	
	/**
	 * Normalizes a path.
	 * @param path The path
	 * @return The normalized path.
	 */
	protected String normalize(String path) {
		return PathUtils.removeLeadingSlash(path);
	}
	
	public void printInfo() {
		printInfo(Logger.system);
	}
	
	public void printInfo(Logger logger) {
		logger.log(getClass().getSimpleName() + ": " + count());
	}
	
	public final void addListener(AssetPoolEventType assetPoolEventType, EventListener<AssetPoolEvent<T>> listener) {
		eventDispatcher.addListener(assetPoolEventType, listener);
	}
	
	public final void removeListener(AssetPoolEventType assetPoolEventType, EventListener<AssetPoolEvent<T>> listener) {
		eventDispatcher.removeListener(assetPoolEventType, listener);
	}
	
}
