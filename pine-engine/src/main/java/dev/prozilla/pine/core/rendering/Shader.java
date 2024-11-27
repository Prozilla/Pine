package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.Lifecycle;

import java.io.*;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

/**
 * Represents an OpenGL shader.
 */
public class Shader implements Lifecycle {

    /**
     * Stores the handle of the shader.
     */
    private final int id;

    /**
     * Creates a shader with specified type. The type in the tutorial should be
     * either <code>GL_VERTEX_SHADER</code> or <code>GL_FRAGMENT_SHADER</code>.
     * @param type Type of the shader
     */
    public Shader(int type) {
        id = glCreateShader(type);
    }

    /**
     * Sets the source code of this shader.
     * @param source GLSL Source Code for the shader
     */
    public void source(CharSequence source) {
        glShaderSource(id, source);
    }

    /**
     * Compiles the shader and checks its status afterward.
     */
    public void compile() {
        glCompileShader(id);

        checkStatus();
    }

    /**
     * Checks if the shader was compiled successfully.
     */
    private void checkStatus() throws RuntimeException {
        int status = glGetShaderi(id, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(id));
        }
    }

    /**
     * Deletes the shader.
     */
    @Override
    public void destroy() {
        glDeleteShader(id);
    }

    /**
     * Getter for the shader ID.
     * @return Handle of this shader
     */
    public int getId() {
        return id;
    }

    /**
     * Creates a shader with specified type and source and compiles it. The type
     * in the tutorial should be either <code>GL_VERTEX_SHADER</code> or
     * <code>GL_FRAGMENT_SHADER</code>.
     * @param type   Type of the shader
     * @param source Source of the shader
     * @return Compiled Shader from the specified source
     */
    public static Shader createShader(int type, CharSequence source) {
        Shader shader = new Shader(type);
        shader.source(source);
        shader.compile();

        return shader;
    }

    /**
     * Loads a shader from a file.
     * @param type Type of the shader
     * @param path File path of the shader
     * @return Compiled Shader from specified file
     */
    public static Shader loadShader(int type, String path) {
        StringBuilder builder = new StringBuilder();
        
        System.out.println("Loading shader: " + path);
        
        try (InputStream in = Shader.class.getResourceAsStream(path)) {
	        assert in != null;
	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                builder.append(line).append("\n");
	            }
	        }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load shader file"
                + System.lineSeparator() + ex.getMessage());
        }
        CharSequence source = builder.toString();

        return createShader(type, source);
    }

}
