package dev.prozilla.pine.core;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.asset.image.Image;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.Platform;
import dev.prozilla.pine.common.util.BooleanUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.config.WindowConfig;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import java.util.StringJoiner;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Represents a GLFW window object.
 */
public class Window implements Initializable, Destructible, Printable {
	
	/** Handle of the window */
	private long id;
	
	public int width;
	public int height;
	
	private GLFWWindowSizeCallback windowSizeCallback;
	protected boolean isInitialized;
	
	private final Application application;
	private final Renderer renderer;
	private final WindowConfig config;
	private final Logger logger;
	
	public Window(Application application) {
		this.application = application;
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
		setHint(WindowHint.RETINA_FRAMEBUFFER, false);
		setVisible(true);
		
		// On MacOS, windows can't be initialized in fullscreen mode
		boolean deferredFullscreen = config.fullscreen.get() && Platform.get() == Platform.MACOS;
		if (deferredFullscreen) {
			config.fullscreen.set(false);
		}
		
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
					width = config.width.get();
					height = config.height.get();
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
		if (config.fullscreen.get()) {
			monitor = glfwGetPrimaryMonitor();
			GLFWVidMode videoMode = glfwGetVideoMode(monitor);
			if (videoMode != null) {
				width = videoMode.width();
				height = videoMode.height();
			}
		} else {
			width = config.width.get();
			height = config.height.get();
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
		if (config.enableVSync.get()) {
			glfwSwapInterval(1);
		}
		
		// Detect window resizes
		glfwSetWindowSizeCallback(id, windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				updateSize(width, height);
			}
		});
		
		isInitialized = true;
		
		if (deferredFullscreen) {
			application.defer(() -> config.fullscreen.set(true));
		}
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
		
		if (config.enableToggleFullscreen.get() && (input.getKey(Key.L_ALT) || input.getKey(Key.R_ALT)) && input.getKeyDown(Key.ENTER)) {
			config.fullscreen.toggle();
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
	 * @return {@code true} if the window should be closed.
	 */
	public boolean shouldClose() {
		if (!isInitialized) {
			return false;
		}
		
		return glfwWindowShouldClose(id);
	}
	
	private void updateSize(int width, int height) {
		this.width = width;
		this.height = height;
		renderer.resize();
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

    public void setFullscreen(boolean fullscreen) {
        config.fullscreen.set(fullscreen);
    }
	
	/**
	 * Toggles the fullscreen mode of this window.
	 * @param fullscreen Whether to enable or disable fullscreen mode
	 */
	public void setFullscreen(boolean fullscreen) {
		config.fullscreen.set(fullscreen);
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
	 * Sets the opacity of the entire window.
	 * @param opacity The opacity, in the range of {@code 0f} and {@code 1f}
	 */
	public void setOpacity(float opacity) {
		checkStatus();
		glfwSetWindowOpacity(id, opacity);
	}
	
	/**
	 * Sets the value of the {@link WindowHint#DECORATED} window hint.
	 * @param decorated The value to use
	 */
	public void setDecorated(boolean decorated) {
		setHint(WindowHint.DECORATED, decorated);
	}
	
	/**
	 * Sets the value of the {@link WindowHint#VISIBLE} window hint.
	 * @param visible The value to use
	 */
	public void setVisible(boolean visible) {
		setHint(WindowHint.VISIBLE, visible);
	}
	
	/**
	 * Sets the value of the {@link WindowHint#ENABLE_TRANSPARENT_FRAMEBUFFER} window hint.
	 * @param enableTransparentFramebuffer The value to use
	 */
	public void setEnableTransparentFramebuffer(boolean enableTransparentFramebuffer) {
		setHint(WindowHint.ENABLE_TRANSPARENT_FRAMEBUFFER, enableTransparentFramebuffer);
	}
	
	/**
	 * Sets the value of the {@link WindowHint#RESIZABLE} window hint.
	 * @param resizable The value to use
	 */
	public void setResizable(boolean resizable) {
		setHint(WindowHint.RESIZABLE, resizable);
	}
	
	/**
	 * Sets the value of the {@link WindowHint#FOCUSED} window hint.
	 * @param focused The value to use
	 */
	public void setFocused(boolean focused) {
		setHint(WindowHint.FOCUSED, focused);
	}
	
	/**
	 * Sets the value of the {@link WindowHint#FLOATING} window hint.
	 * @param floating The value to use
	 */
	public void setFloating(boolean floating) {
		setHint(WindowHint.FLOATING, floating);
	}
	
	/**
	 * Sets the value of the {@link WindowHint#MAXIMIZED} window hint.
	 * @param maximized The value to use
	 */
	public void setMaximized(boolean maximized) {
		setHint(WindowHint.MAXIMIZED, maximized);
	}
	
	/**
	 * Sets the value of the {@link WindowHint#CENTER_CURSOR} window hint.
	 * @param centerCursor The value to use
	 */
	public void setCenterCursor(boolean centerCursor) {
		setHint(WindowHint.CENTER_CURSOR, centerCursor);
	}
	
	/**
	 * Sets the value of the given window hint.
	 * @param hint The window hint to set the value of
	 * @param value The value to use
	 * @see #setHint(int, int)
	 */
	public void setHint(WindowHint hint, boolean value) {
		setHint(hint, BooleanUtils.toInt(value));
	}
	
	/**
	 * Sets the value of the given window hint.
	 * @param hint The window hint to set the value of
	 * @param value The value to use
	 * @see #setHint(int, int)
	 */
	public void setHint(WindowHint hint, int value) {
		Checks.isNotNull(hint, "hint");
		setHint(hint.getValue(), value);
	}
	
	/**
	 * Sets the value of the given window hint.
	 * @param hint The window hint to set the value of
	 * @param value The value to use
	 * @see #setHint(int, int)
	 */
	public void setHint(int hint, boolean value) {
		setHint(hint, BooleanUtils.toInt(value));
	}
	
	/**
	 * Sets the value of the given window hint.
	 *
	 * <p>The window hints determine how the next window will be created.
	 * Invalid values will only cause an error when the next window is created.</p>
	 *
	 * <p>Some hints will only have an effect on specific platforms and will be ignored on other platforms.</p>
	 * @param hint The window hint to set the value of
	 * @param value The value to use
	 */
	public void setHint(int hint, int value) {
		if (!WindowHint.isValid(hint)) {
			logger.log("Unknown window hint: " + hint);
		}
		glfwWindowHint(hint, value);
	}
	
	/**
	 * Resets all window hints to their default values.
	 */
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
	
	public float getPixelRatioX() {
		return renderer.getWidth() / (float)width;
	}
	
	public float getPixelRatioY() {
		return renderer.getHeight() / (float)height;
	}
	
	@Override
	public @NotNull String toString() {
		StringJoiner stringJoiner = new StringJoiner("\n");
		
		stringJoiner.add(Logger.formatHeader("Window Info"));
		stringJoiner.add("Title: " + config.title.getValue());
		stringJoiner.add(String.format("Window: %sx%s", width, height));
		stringJoiner.add(String.format("Viewport: %sx%s", renderer.getWidth(), renderer.getHeight()));
		stringJoiner.add(String.format("Pixel ratio: %s, %s", getPixelRatioX(), getPixelRatioY()));
		stringJoiner.add("Fullscreen: " + config.fullscreen.get());
		stringJoiner.add("Show decorations: " + config.showDecorations.get());
		
		return stringJoiner.toString();
	}
	
}
