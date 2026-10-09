package dev.prozilla.pine.core.rendering;

public enum RenderMode {
	/** The default rendering mode, uses colors, textures and materials. */
	DEFAULT,
	/** A rendering mode that highlights the outlines of triangles. */
	WIREFRAME,
	/** A rendering mode that uses the depth value as the color for each vertex. */
	DEPTH,
	NORMAL,
}
