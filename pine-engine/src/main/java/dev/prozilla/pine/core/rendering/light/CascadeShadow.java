package dev.prozilla.pine.core.rendering.light;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.matrix.Matrix4f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector4f;

/**
 * Light matrices and split distance for a single cascade of a shadow map.
 */
public class CascadeShadow {
	
	public final Matrix4f viewMatrix;
	public final Matrix4f projectionMatrix;
	public final Matrix4f projViewMatrix;
	/** The view-space depth at which this cascade ends (negative, since the camera looks along -z). */
	public float splitDistance;
	
	/** The amount of cascades. Must match NUM_CASCADES in lit.frag. */
	public static final int COUNT = 3;
	/** Blend between uniform (0) and logarithmic (1) distribution of the split distances. */
	public static final float SPLIT_LAMBDA = 0.5f;
	/** How far toward the sun objects outside of a cascade can still cast shadows into it. */
	public static final float CASTER_RANGE = 50f;
	private static final float MIN_NEAR = 0.01f;
	
	/** Corners of the view frustum in NDC space: 4 near corners, followed by the 4 matching far corners. */
	private static final float[][] NDC_CORNERS = {
		{ -1, 1, -1 }, { 1, 1, -1 }, { 1, -1, -1 }, { -1, -1, -1 },
		{ -1, 1, 1 }, { 1, 1, 1 }, { 1, -1, 1 }, { -1, -1, 1 }
	};
	
	public CascadeShadow() {
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
		projViewMatrix = new Matrix4f();
	}
	
	/**
	 * Fits the cascades to the view frustum of a camera.
	 * @param cascades The cascades to update
	 * @param view The view matrix of the camera
	 * @param projection The projection matrix of the camera
	 * @param near The near clip plane of the camera
	 * @param far The far clip plane of the camera
	 * @param shadowDistance The maximum distance from the camera that receives shadows
	 * @param toLight The direction from the world toward the light
	 */
	public static void update(CascadeShadow[] cascades, Matrix4f view, Matrix4f projection,
	                          float near, float far, float shadowDistance, Vector3f toLight) {
		float length = MathUtils.sqrt(toLight.x * toLight.x + toLight.y * toLight.y + toLight.z * toLight.z);
		if (length <= 0f) {
			return;
		}
		float lx = toLight.x / length;
		float ly = toLight.y / length;
		float lz = toLight.z / length;
		// Avoid a degenerate view matrix when the sun is directly overhead
		Vector3f up = Math.abs(ly) > 0.99f ? new Vector3f(0, 0, 1) : new Vector3f(0, 1, 0);
		
		float maxZ = Math.max(Math.min(far, shadowDistance), near + MIN_NEAR);
		float logNear = Math.max(near, MIN_NEAR);
		float range = maxZ - near;
		float ratio = maxZ / logNear;
		float clipRange = far - near;
		
		// Project frustum corners into world space
		Matrix4f inverse = projection.clone().multiply(view).invert();
		float[][] corners = new float[8][3];
		for (int i = 0; i < 8; i++) {
			Vector4f corner = new Vector4f(NDC_CORNERS[i][0], NDC_CORNERS[i][1], NDC_CORNERS[i][2], 1f);
			inverse.multiply(corner);
			corners[i][0] = corner.x / corner.w;
			corners[i][1] = corner.y / corner.w;
			corners[i][2] = corner.z / corner.w;
		}
		
		float lastSplit = 0f;
		for (int i = 0; i < COUNT; i++) {
			// Split distance, based on https://developer.nvidia.com/gpugems/GPUGems3/gpugems3_ch10.html
			float p = (i + 1) / (float)COUNT;
			float logSplit = logNear * (float)Math.pow(ratio, p);
			float uniformSplit = near + range * p;
			float splitDepth = SPLIT_LAMBDA * (logSplit - uniformSplit) + uniformSplit;
			float split = (splitDepth - near) / clipRange;
			
			// Slice of the frustum for this cascade, and its center
			float[][] slice = new float[8][3];
			float[] center = new float[3];
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 3; k++) {
					float step = corners[j + 4][k] - corners[j][k];
					slice[j][k] = corners[j][k] + step * lastSplit;
					slice[j + 4][k] = corners[j][k] + step * split;
					center[k] += (slice[j][k] + slice[j + 4][k]) / 8f;
				}
			}
			
			// Bounding sphere, rounded to keep the size stable while the camera rotates
			float radius = 0f;
			for (float[] corner : slice) {
				float dx = corner[0] - center[0];
				float dy = corner[1] - center[1];
				float dz = corner[2] - center[2];
				radius = Math.max(radius, MathUtils.sqrt(dx * dx + dy * dy + dz * dz));
			}
			radius = (float)Math.ceil(radius * 16f) / 16f;
			
			// Light matrices
			CascadeShadow cascade = cascades[i];
			float distance = radius + CASTER_RANGE;
			Vector3f eye = new Vector3f(center[0] + lx * distance, center[1] + ly * distance, center[2] + lz * distance);
			cascade.viewMatrix.lookAt(eye, new Vector3f(center[0], center[1], center[2]), up);
			cascade.projectionMatrix.setIdentity().orthographic(-radius, radius, -radius, radius, 0, radius + distance);
			cascade.projViewMatrix.set(cascade.projectionMatrix).multiply(cascade.viewMatrix);
			cascade.splitDistance = -splitDepth;
			
			lastSplit = split;
		}
	}
	
}