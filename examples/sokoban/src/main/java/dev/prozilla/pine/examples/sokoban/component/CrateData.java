package dev.prozilla.pine.examples.sokoban.component;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.component.Component;

public class CrateData extends Component {
	
	public int type;
	
	public CrateData(int type) {
		this.type = type;
	}
	
	public static TextureAsset getNormalSprite(int type) {
		return getSprite(getNormalSpriteIndex(type));
	}
	
	public static TextureAsset getSunkenSprite(int type) {
		return getSprite(getSunkenSpriteIndex(type));
	}
	
	private static int getNormalSpriteIndex(int type) {
		return type != 0 ? type + 1 : type + 6;
	}
	
	private static int getSunkenSpriteIndex(int type) {
		return type != 0 ? type + 6 : type + 1;
	}
	
	private static TextureAsset getSprite(int index) {
		return AssetPools.textures.load(String.format("images/crates/crate_%02d.png", index));
	}
	
}
