package dev.prozilla.pine.core;

import dev.prozilla.pine.common.asset.image.Image;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.util.BooleanUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.config.WindowConfig;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.GLFW.*;
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
	protected boolean isInitialized;
	
	private final Renderer renderer;
	private final WindowConfig config;
	private final Logger logger;
	
	public Window(Application application) {
		renderer = application.getRenderer();
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
		setHint(WindowHint.GL_VERSION_MAJOR, 4);
		setHint(WindowHint.GL_VERSION_MINOR, 1);
		setHint(WindowHint.GL_FORWARD_COMPATIBLE, true);
		setHint(WindowHint.GL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		setVisible(true);
		
		// Read config options
		if (!isInitialized) {
			config.showDecorations.read(this::setDecorated);
			config.title.read((title) -> {
				if (isInitialized) {
					glfwSetWindowTitle(id, title);
				}
			});
			config.fullscreen.addObserver((fullscreen) -> {
				long monitor = glfwGetPrimaryMonitor();
				GLFWVidMode videoMode = glfwGetVideoMode(monitor);
				if (fullscreen && videoMode != null) {
					glfwSetWindowMonitor(id, monitor, 0, 0,
						videoMode.width(), videoMode.height(), videoMode.refreshRate());
				} else {
					width = config.width.getValue();
					height = config.height.getValue();
					if (videoMode != null) {
						int x = (videoMode.width() - width) / 2;
						int y = (videoMode.height() - height) / 2;
						glfwSetWindowMonitor(id, NULL, x, y, width, height, 0);
					} else {
						glfwSetWindowMonitor(id, NULL, 0, 0, width, height, 0);
					}
				}
			});
		}
		
		// Prepare fullscreen window
		long monitor = NULL;
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
				updateSize();
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
	
	public void input(Input input) {
		if (!isInitialized) {
			return;
		}
		
		if (config.enableToggleFullscreen.getValue() && (input.getKey(Key.L_ALT) || input.getKey(Key.R_ALT)) && input.getKeyDown(Key.ENTER)) {
			config.fullscreen.setValue(!config.fullscreen.getValue());
		}
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
	
	private void updateSize() {
		renderer.resize();
		this.width = renderer.getViewportWidth();
		this.height = renderer.getViewportHeight();
	}
	
	public Vector2i getSize() {
		return new Vector2i(width, height);
	}
	
	public int getWidth() {
		return width;
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
