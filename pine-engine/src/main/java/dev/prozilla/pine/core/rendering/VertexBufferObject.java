package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.Lifecycle;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * Represents a Vertex Buffer Object (VBO).
 */
public class VertexBufferObject implements Lifecycle {

    /**
     * Stores the handle of the VBO.
     */
    private final int id;

    /**
     * Creates a Vertex Buffer Object (VBO).
     */
    public VertexBufferObject() {
        id = glGenBuffers();
    }

    /**
     * Binds this VBO with specified target. The target in the tutorial should
     * be <code>GL_ARRAY_BUFFER</code> most of the time.
     * @param target Target to bind
     */
    public void bind(int target) {
        glBindBuffer(target, id);
    }

    /**
     * Upload vertex data to this VBO with specified target, data and usage. The
     * target in the tutorial should be <code>GL_ARRAY_BUFFER</code> and usage
     * should be <code>GL_STATIC_DRAW</code> most of the time.
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    public void uploadData(int target, FloatBuffer data, int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Upload null data to this VBO with specified target, size and usage. The
     * target in the tutorial should be <code>GL_ARRAY_BUFFER</code> and usage
     * should be <code>GL_STATIC_DRAW</code> most of the time.
     * @param target Target to upload
     * @param size   Size in bytes of the VBO data store
     * @param usage  Usage of the data
     */
    public void uploadData(int target, long size, int usage) {
        glBufferData(target, size, usage);
    }

    /**
     * Upload sub data to this VBO with specified target, offset and data. The
     * target in the tutorial should be <code>GL_ARRAY_BUFFER</code> most of the
     * time.
     * @param target Target to upload
     * @param offset Offset where the data should go in bytes
     * @param data   Buffer with the data to upload
     */
    public void uploadSubData(int target, long offset, FloatBuffer data) {
        glBufferSubData(target, offset, data);
    }

    /**
     * Upload element data to this EBO with specified target, data and usage.
     * The target in the tutorial should be <code>GL_ELEMENT_ARRAY_BUFFER</code>
     * and usage should be <code>GL_STATIC_DRAW</code> most of the time.
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    public void uploadData(int target, IntBuffer data, int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Deletes this VBO.
     */
    @Override
    public void destroy() {
        glDeleteBuffers(id);
    }

    /**
     * Getter for the Vertex Buffer Object ID.
     * @return Handle of the VBO
     */
    public int getId() {
        return id;
    }

}
