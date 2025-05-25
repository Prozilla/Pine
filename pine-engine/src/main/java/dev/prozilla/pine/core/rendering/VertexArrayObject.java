package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.lifecycle.Destructable;

import static org.lwjgl.opengl.GL30.*;

/**
 * Represents a Vertex Array Object (VAO).
 */
public class VertexArrayObject implements Destructable {

    /**
     * Stores the handle of the VAO.
     */
    private final int id;

    /**
     * Creates a Vertex Array Object (VAO).
     */
    public VertexArrayObject() {
        id = glGenVertexArrays();
    }

    /**
     * Binds the VAO.
     */
    public void bind() {
        glBindVertexArray(id);
    }

    /**
     * Deletes the VAO.
     */
    @Override
    public void destroy() {
        glDeleteVertexArrays(id);
    }

    /**
     * Getter for the Vertex Array Object ID.
     * @return Handle of the VAO
     */
    public int getId() {
        return id;
    }

}
