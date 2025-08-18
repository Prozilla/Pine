package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;

public class BorderImage extends Component {
	
	public TextureBase texture;
	public Vector4f slice;
	public boolean fill;
	
	public BorderImage(String texturePath) {
		this(AssetPools.textures.load(texturePath));
	}
	
	public BorderImage(TextureBase texture) {
		this.texture = Checks.isNotNull(texture, "texture");
		slice = new Vector4f();
		fill = false;
	}

}
