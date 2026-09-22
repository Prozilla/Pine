package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.event.Event;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.system.DirectoryWatcher;
import dev.prozilla.pine.common.system.FileWatcher;
import dev.prozilla.pine.common.system.ResourceUtils;
import dev.prozilla.pine.common.util.checks.Checks;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HotStyleSheet extends StyleSheet implements FileWatcher {
	
	protected final List<StyledProperty<?, ?, ?, ?>> trackedProperties;
	
	private final Path filePath;
	private final DirectoryWatcher directoryWatcher;
	private final EventListener<Event<DirectoryWatcher.EventType, String>> listener;
	
	public HotStyleSheet(DirectoryWatcher directoryWatcher, String path) {
		super(Checks.isNotNull(path, "path"));
		filePath = ResourceUtils.getResourceFilePath(path);
		this.directoryWatcher = Checks.isNotNull(directoryWatcher, "directoryWatcher");
		trackedProperties = new ArrayList<>();
		
		listener = directoryWatcher.watch(this);
	}
	
	@Override
	protected void trackProperty(StyledProperty<?, ?, ?, ?> property) {
		trackedProperties.add(property);
	}
	
	@Override
	public void onFileChange(Event<DirectoryWatcher.EventType, String> event) {
		reload();
	}
	
	public void reload() {
		AssetPools.styleSheets.reload(this);
		for (StyledProperty<?, ?, ?, ?> property : new ArrayList<>(trackedProperties)) {
			reloadProperty(property);
		}
	}
	
	protected <T> void reloadProperty(StyledProperty<T, ?, ?, ?> property) {
		List<StyleSheet> sources = property.getSources();
		if (sources == null) {
			sources = List.of(this);
		}
		
		StyleSheet combined = StyleSheet.mergeAll(sources);
		Style<T, ?> style = combined.getStyle(property.name, false);
		property.applyStyle(style);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		trackedProperties.clear();
		directoryWatcher.removeFileChangeListener(listener);
	}
	
	@Override
	public InputStream createInputStream() {
		return ResourceUtils.createResourceFileInputStream(path, filePath);
	}
	
	@Override
	public HotStyleSheet toHotStyleSheet(DirectoryWatcher directoryWatcher) {
		return this;
	}
	
	public static HotStyleSheet fromStyleSheet(DirectoryWatcher directoryWatcher, StyleSheet styleSheet) {
		HotStyleSheet hotStyleSheet = new HotStyleSheet(directoryWatcher, styleSheet.getPath());
		styleSheet.transmit(hotStyleSheet);
		return hotStyleSheet;
	}
	
}
