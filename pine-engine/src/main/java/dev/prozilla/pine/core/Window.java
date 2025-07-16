package dev.prozilla.pine.core;

import dev.prozilla.pine.common.asset.image.Image;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.BooleanUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.state.config.WindowConfig;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Represents a GLFW window object.
 */
public class Window implements Initializable, Destructible {
	
	/** Handle of the window */
	private long id;
	
	public int width;
	public int height;
	
	private GLFWWindowSizeCallback windowSizeCallback;
	private boolean isInitialized;
	
	private final WindowConfig config;
	private final Logger logger;
	
	public Window(Application application) {
		config = application.getConfig().window;
		logger = application.logger;
		
		isInitialized = false;
	}
	
	/**
	 * Sets the window hints and creates a GLFW window object.
	 */
	@Override
	public void init() throws RuntimeException {
		// Set window hints
		setDefaultHints();
		setVisible(true);
		
		long monitor = NULL;
		
		// Read config options
		config.showDecorations.read(this::setDecorated);
		config.title.read((title) -> {
			if (isInitialized) {
				glfwSetWindowTitle(id, title);
			}
		});
		
		// Prepare fullscreen window
		if (config.fullscreen.getValue()) {
			monitor = glfwGetPrimaryMonitor();
			GLFWVidMode videoMode = glfwGetVideoMode(monitor);
			if (videoMode != null) {
				width = videoMode.width();
				height = videoMode.height();
			}
		} else {
			width = config.width.getValue();
			height = config.height.getValue();
		}
		
		// Create window
		String title = config.title.getValue();
		id = glfwCreateWindow(width, height, title, monitor, NULL);
		if (id == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}
		glfwMakeContextCurrent(id);
		
		// Enable VSync
		if (config.enableVSync.getValue()) {
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
	
	public long getId() {
		return id;
	}
	
	/**
	 * Updates the title of this window.
	 * @param title Title
	 */
	public void setTitle(String title) {
		config.title.setValue(title);
	}
	
	/**
	 * Updates the icons of this window.
	 * Uses the first element of the images array as the default icon.
	 * @param images Array of icon images
	 */
	public void setIcons(Image[] images) {
		checkStatus();
		
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
	
	/**
	 * Sets the opacity of the entire window
	 * @param opacity The opacity, in the range of {@code 0f} and {@code 1f}
	 */
	public void setOpacity(float opacity) {
		checkStatus();
		glfwSetWindowOpacity(id, opacity);
	}
	
	public void setDecorated(boolean decorated) {
		setHint(WindowHint.DECORATED, decorated);
	}
	
	public void setVisible(boolean visible) {
		setHint(WindowHint.VISIBLE, visible);
	}
	
	public void setEnableTransparentFramebuffer(boolean transparentFramebuffer) {
		setHint(WindowHint.ENABLE_TRANSPARENT_FRAMEBUFFER, transparentFramebuffer);
	}
	
	public void setResizable(boolean resizable) {
		setHint(WindowHint.RESIZABLE, resizable);
	}
	
	public void setFocused(boolean focused) {
		setHint(WindowHint.FOCUSED, focused);
	}
	
	public void setFloating(boolean floating) {
		setHint(WindowHint.FLOATING, floating);
	}
	
	public void setMaximized(boolean maximized) {
		setHint(WindowHint.MAXIMIZED, maximized);
	}
	
	public void setCenterCursor(boolean centerCursor) {
		setHint(WindowHint.CENTER_CURSOR, centerCursor);
	}
	
	public void setHint(WindowHint hint, boolean value) {
		setHint(hint, BooleanUtils.toInt(value));
	}
	
	public void setHint(WindowHint hint, int value) {
		Checks.isNotNull(hint, "hint");
		setHint(hint.getValue(), value);
	}
	
	public void setHint(int hint, boolean value) {
		setHint(hint, BooleanUtils.toInt(value));
	}
	
	public void setHint(int hint, int value) {
		if (!WindowHint.isValid(hint)) {
			logger.log("Unknown window hint: " + hint);
		}
		glfwWindowHint(hint, value);
	}
	
	public void setDefaultHints() {
		glfwDefaultWindowHints();
	}
	
	/**
	 * Checks if the window has been initialized.
	 * @throws IllegalStateException If the window has not been initialized yet.
	 */
	protected void checkStatus() throws IllegalStateException {
		if (!isInitialized) {
			throw new IllegalStateException("window has not been initialized yet");
		}
	}
	
}
