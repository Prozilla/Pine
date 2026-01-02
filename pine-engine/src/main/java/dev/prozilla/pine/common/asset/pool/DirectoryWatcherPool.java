package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.system.DirectoryWatcher;
import dev.prozilla.pine.common.system.PathUtils;

public class DirectoryWatcherPool extends AssetPool<DirectoryWatcher> {
	
	@Override
	public DirectoryWatcher load(String path) {
		return super.load(path);
	}
	
	@Override
	protected DirectoryWatcher createAsset(String path) {
		return new DirectoryWatcher(path);
	}
	
	@Override
	protected String normalize(String path) {
		return PathUtils.onlyTrailingSlash(path);
	}
	
}
