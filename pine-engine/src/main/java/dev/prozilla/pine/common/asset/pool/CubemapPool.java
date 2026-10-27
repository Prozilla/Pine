package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.image.Cubemap;
import dev.prozilla.pine.common.asset.image.Image;
import dev.prozilla.pine.common.asset.image.Texture;

public final class CubemapPool extends AssetPool<Cubemap> implements MultiAssetLoader<Cubemap> {
	
	private final ImagePool imagePool;
	
	// Cubemap parameters
	private Texture.Wrap textureWrap;
	private Texture.Filter textureFilter;
	
	// Global defaults
	private Texture.Wrap defaultTextureWrap;
	private Texture.Filter defaultTextureFilter;
	
	public CubemapPool(ImagePool imagePool) {
		this.imagePool = imagePool;
		
		defaultTextureWrap = Texture.DEFAULT_WRAP;
		defaultTextureFilter = Texture.DEFAULT_FILTER;
		
		textureWrap = defaultTextureWrap;
		textureFilter = defaultTextureFilter;
	}
	
	public Cubemap load(String path, Texture.Wrap wrap) {
		textureWrap = wrap;
		return load(path);
	}
	
	public Cubemap load(String path, Texture.Filter filter) {
		textureFilter = filter;
		return load(path);
	}
	
	public Cubemap load(String path, Texture.Wrap wrap, Texture.Filter filter) {
		textureWrap = wrap;
		textureFilter = filter;
		return load(path);
	}
	
	@Override
	public Cubemap load(String path) {
		return super.load(path);
	}
	
	@Override
	protected Cubemap createAsset(String path) {
		Cubemap.Target[] targets = Cubemap.Target.values();
		Image[] images = new Image[targets.length];
		for (int i = 0; i < images.length; i++) {
			String facePath = path.replaceFirst("right|left|top|bottom|front|back", targets[i].toString().toLowerCase());
			images[i] = imagePool.load(facePath, false);
		}
		
		return new Cubemap(path, images);
	}
	
	@Override
	protected void prepareNext() {
		super.prepareNext();
		textureWrap = defaultTextureWrap;
		textureFilter = defaultTextureFilter;
	}
	
	@Override
	protected boolean remove(String path) {
		boolean removed = super.remove(path);
		if (removed) {
			imagePool.remove(path);
		}
		return removed;
	}
	
	public void setDefaultTextureWrap(Texture.Wrap defaultTextureWrap) {
		this.defaultTextureWrap = defaultTextureWrap;
		textureWrap = defaultTextureWrap;
	}
	
	public void setDefaultTextureFilter(Texture.Filter defaultTextureFilter) {
		this.defaultTextureFilter = defaultTextureFilter;
		textureFilter = defaultTextureFilter;
	}
	
}
