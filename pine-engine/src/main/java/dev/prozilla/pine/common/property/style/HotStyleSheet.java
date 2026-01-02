package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.event.Event;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.system.DirectoryWatcher;
import dev.prozilla.pine.common.system.FileWatcher;
import dev.prozilla.pine.common.system.ResourceUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.ui.Node;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HotStyleSheet extends StyleSheet implements FileWatcher {
	
	protected final List<HotStyledProperty<?, ?>> hotStyledProperties;
	
	private final Path filePath;
	private final DirectoryWatcher directoryWatcher;
	private final EventListener<Event<DirectoryWatcher.EventType, String>> listener;
	
	public HotStyleSheet(DirectoryWatcher directoryWatcher, String path) {
		super(Checks.isNotNull(path, "path"));
		filePath = ResourceUtils.getResourceFilePath(path);
		this.directoryWatcher = Checks.isNotNull(directoryWatcher, "directoryWatcher");
		hotStyledProperties = new ArrayList<>();
		
		listener = directoryWatcher.watch(this);
	}
	
	@Override
	protected <T, A extends AdaptiveProperty<T, ?>, P extends StyledProperty<T, ?, A, ?>> P createStyledProperty(StyledPropertyKey<T> name, Node node, A fallbackValue, Style.StyledPropertyFactory<T, A, P> factory) {
		P styledProperty = super.createStyledProperty(name, node, fallbackValue, factory);
		if (styledProperty != null) {
			hotStyledProperties.add(new HotStyledProperty<>(styledProperty, fallbackValue));
		}
		return styledProperty;
	}
	
	@Override
	public void onFileChange(Event<DirectoryWatcher.EventType, String> event) {
		reload();
	}
	
	public void reload() {
		AssetPools.styleSheets.reload(this);
		for (HotStyledProperty<?, ?> hotStyledProperty : hotStyledProperties) {
			reloadStyledProperty(hotStyledProperty);
		}
	}
	
	protected <T, A extends AdaptiveProperty<T, ?>> void reloadStyledProperty(HotStyledProperty<T, A> hotStyledProperty) {
		reloadStyledProperty(hotStyledProperty.getStyledProperty(), hotStyledProperty.getAdaptiveType());
	}
	
	protected <T, A extends AdaptiveProperty<T, ?>, P extends StyledProperty<T, ?, A, ?>> void reloadStyledProperty(P styledProperty, Class<A> type) {
		Style<T, A> style = getStyle(styledProperty.name, false, type);
		styledProperty.applyStyle(style);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		hotStyledProperties.clear();
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
	
	public static class HotStyledProperty<T, A extends AdaptiveProperty<T, ?>> {
		
		private final StyledProperty<T, ?, A, ?> styledProperty;
		private final Class<A> adaptiveType;
		
		@SuppressWarnings("unchecked")
		public HotStyledProperty(StyledProperty<T, ?, A, ?> styledProperty, A fallbackValue) {
			this(styledProperty, (Class<A>)fallbackValue.getClass());
		}
		
		public HotStyledProperty(StyledProperty<T, ?, A, ?> styledProperty, Class<A> adaptiveType) {
			this.styledProperty = styledProperty;
			this.adaptiveType = adaptiveType;
		}
		
		public StyledProperty<T, ?, A, ?> getStyledProperty() {
			return styledProperty;
		}
		
		public Class<A> getAdaptiveType() {
			return adaptiveType;
		}
		
	}
	
}
