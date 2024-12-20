package dev.prozilla.pine.common.math.vector;

import java.nio.FloatBuffer;

/**
 * 3-dimensional vector with floating point precision. GLSL equivalent to <code>vec3</code>.
 */
public class Vector3f implements VectorFloat<Vector3f> {

    public float x;
    public float y;
    public float z;
    
    /**
     * Creates a default 3-dimensional vector with all values set to <code>0f</code>.
     */
    public Vector3f() {
        this(0f, 0f, 0f);
    }
    
    /**
     * Creates a 3-dimensional vector with given values.
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3f add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    @Override
    public Vector3f add(Vector3f vector3f) {
        x += vector3f.x;
        y += vector3f.y;
        z += vector3f.z;
        return this;
    }
    
    @Override
    public Vector3f scale(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }
    
    @Override
    public float lengthSquared() {
        return x * x + y * y + z * z;
    }
    
    @Override
    public float dot(Vector3f vector3f) {
        return x * vector3f.x + y * vector3f.y + z * vector3f.z;
    }
    
    @Override
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y).put(z);
        buffer.flip();
    }
    
    /**
     * Converts this vector to a string representation in the format "(x,y,z)".
     */
    @Override
    public String toString() {
        return String.format("(%s,%s,%s)", x, y, z);
    }
}
