package dev.prozilla.pine.core.rendering;

public enum RenderMode {
	/** The normal rendering mode, uses colors and textures. */
	NORMAL,
	/** A rendering mode that only shows the outlines of triangles. */
	WIREFRAME,
	/** A rendering mode that uses the depth value as the color for each vertex. */
	DEPTH
}
