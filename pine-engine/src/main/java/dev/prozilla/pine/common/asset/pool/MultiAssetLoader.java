package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.Asset;

import java.util.ArrayList;
import java.util.List;

public interface MultiAssetLoader<T extends Asset> {
	
	default List<T> loadAll(String... paths) {
		List<T> assets = new ArrayList<>();
		for (String path : paths) {
			assets.add(load(path));
		}
		return assets;
	}
	
	default List<T> loadAll(List<String> paths) {
		List<T> assets = new ArrayList<>();
		for (String path : paths) {
			assets.add(load(path));
		}
		return assets;
	}
	
	T load(String path);
	
}
