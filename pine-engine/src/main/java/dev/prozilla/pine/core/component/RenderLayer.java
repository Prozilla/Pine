package dev.prozilla.pine.core.component;

// TODO: Add way to visualize this in 3D by scaling the depth value and adding more space between different layers/parents.
/**
 * Orders entities into layers on the z-axis based on their z-indices.
 */
public class RenderLayer extends Component {
	
	/** Z-index in the world, highest values are rendered first. */
	private int zIndex;
	/** If true, sets the depth of children to a lower value than the parent. */
	private boolean renderChildrenBelow;
	
	public RenderLayer() {
		this(false);
	}
	
	public RenderLayer(boolean renderChildrenBelow) {
		this.renderChildrenBelow = renderChildrenBelow;
		zIndex = 0;
	}
	
	public void setRenderChildrenBelow(boolean renderChildrenBelow) {
		if (this.renderChildrenBelow == renderChildrenBelow) {
			return;
		}
		
		this.renderChildrenBelow = renderChildrenBelow;
		getWorld().updateRenderLayers();
	}
	
	/**
	 * Temporarily sets the z-index of this entity to its parent's z-index, until the proper z-index is calculated.
	 */
	public void updateParent() {
		RenderLayer parentLayer = getComponentAbove(RenderLayer.class);
		if (parentLayer != null) {
			zIndex = parentLayer.getzIndex();
		}
	}
	
	/**
	 * Calculates the z-indices of this entity and its children.
	 * @param zIndex Z-index value before calculation
	 * @return Z-index value after calculation
	 */
	public int calculateZIndex(int zIndex) {
		if (!renderChildrenBelow) {
			this.zIndex = zIndex++;
		}
		
		for (Transform child : getTransform().children) {
			RenderLayer childLayer = child.getComponent(RenderLayer.class);
			if (childLayer != null) {
				zIndex = childLayer.calculateZIndex(zIndex);
			} else {
				for (RenderLayer grandChildLayer : child.getComponentsBelow(RenderLayer.class, ComponentQuery.NEAREST_PATHS)) {
					zIndex = grandChildLayer.calculateZIndex(zIndex);
				}
			}
		}
		
		if (renderChildrenBelow) {
			this.zIndex = zIndex++;
		}
		
		getTransform().position.z = getDepth();
		return zIndex;
	}
	
	public int getzIndex() {
		return zIndex;
	}
	
	/**
	 * @return Depth value between <code>0f</code> and <code>1f</code> based on the z-index of this entity.
	 */
	public float getDepth() {
		return ((float)zIndex / getWorld().maxDepth) * getWorld().depthMultiplier;
	}
	
}
