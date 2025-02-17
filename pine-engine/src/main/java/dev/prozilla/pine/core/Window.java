package dev.prozilla.pine.core;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.system.resource.Image;
import dev.prozilla.pine.common.util.Numbers;
import dev.prozilla.pine.core.state.config.WindowConfig;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Represents a GLFW window object.
 */
public class Window implements Lifecycle {
	
	/** Handle of the window */
	public long id;
	
	public int width;
	public int height;
	
	protected String title;
	
	private GLFWWindowSizeCallback windowSizeCallback;
	private boolean isInitialized;
	
	private final Application application;
	
	public Window(Application application, int width, int height, String title) {
		this.application = application;
		this.width = Numbers.requirePositive(width, "Window width must be a positive value.");
		this.height = Numbers.requirePositive(height, "Window height must be a positive value.");
		this.title = Objects.requireNonNull(title, "Window title must not be null.");
		
		isInitialized = false;
	}
	
	/**
	 * Sets the window hints and creates a GLFW window object.
	 */
	@Override
	public void init() throws RuntimeException {
		// Set window hints
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		
		long monitor = NULL;
		
		// Read config options
		WindowConfig config = application.getConfig().window;
		config.showDecorations.read(() -> {
			if (config.showDecorations.get()) {
				glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
			} else {
				glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
			}
		});
		
		// Prepare fullscreen window
		if (config.fullscreen.get()) {
			monitor = glfwGetPrimaryMonitor();
			GLFWVidMode videoMode = glfwGetVideoMode(monitor);
			if (videoMode != null) {
				width = videoMode.width();
				height = videoMode.height();
			}
		}
		
		// Create window
		id = glfwCreateWindow(width, height, title, monitor, NULL);
		if (id == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}
		glfwMakeContextCurrent(id);
		
		// Enable VSync
		if (config.enableVSync.get()) {
			glfwSwapInterval(1);
		}
		
		// Detect window resizes
		glfwSetWindowSizeCallback(id, windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				setSize(width, height);
			}
		});
		
		isInitialized = true;
	}
	
	/**
	 * Swaps the buffers and polls the events each frame.
	 */
	@Override
	public void update() {
		if (!isInitialized) {
			return;
		}
		
		glfwSwapBuffers(id);
		glfwPollEvents();
	}
	
	/**
	 * Destroys the window.
	 */
	@Override
	public void destroy() {
		if (!isInitialized) {
			return;
		}
		
		windowSizeCallback.free();
		glfwDestroyWindow(id);
	}
	
	/**
	 * Determines whether the window should be closed.
	 * @return True if the window should be closed
	 */
	public boolean shouldClose() {
		if (!isInitialized) {
			return false;
		}
		
		return glfwWindowShouldClose(id);
	}
	
	private void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
	
	/**
	 * Sets the size of the rendering viewport to match the window.
	 */
	public void refreshSize() {
		glViewport(0, 0, width, height);
	}
	
	private void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
	
	private void setHeight(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return height;
	}
	
	/**
	 * Updates the title of this window.
	 * @param title Title
	 */
	public void setTitle(String title) {
		if (isInitialized) {
			glfwSetWindowTitle(id, title);
		}
		this.title = title;
	}
	
	/**
	 * Updates the icons of this window.
	 * Uses the first element of the images array as the default icon.
	 * @param images Array of icon images
	 */
	public void setIcons(Image[] images) {
		if (!isInitialized) {
			throw new IllegalStateException("Window has not been initialized yet.");
		}
		
		try (GLFWImage.Buffer icons = GLFWImage.malloc(images.length)) {
			for (int i = 0; i < images.length; i++) {
				Image image = images[i];
				icons.position(i)
					.width(image.getWidth())
					.height(image.getHeight())
					.pixels(image.getFlippedImage());
			}
			
			icons.position(0);
			glfwSetWindowIcon(id, icons);
		}
	}
}
