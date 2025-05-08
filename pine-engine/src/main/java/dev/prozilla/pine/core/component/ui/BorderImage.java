package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.image.TextureBase;
import dev.prozilla.pine.core.component.Component;

import java.util.Objects;

public class BorderImage extends Component {
	
	public TextureBase texture;
	public Vector4f slice;
	public boolean fill;
	
	public BorderImage(String texturePath) {
		this(ResourcePool.loadTexture(texturePath));
	}
	
	public BorderImage(TextureBase texture) {
		this.texture = Objects.requireNonNull(texture);
		slice = new Vector4f();
		fill = false;
	}

}
