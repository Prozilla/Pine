package dev.prozilla.pine.core.state;

import dev.prozilla.pine.core.Application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Keeps track of the application's statistics.
 */
public class Tracker {
	
	private int renderedVertices;
	private int totalVertices;
	private String version;
	private int gameObjects;
	
	private final Application application;
	
	public Tracker(Application application) {
		this.application = application;
		
		renderedVertices = 0;
		totalVertices = 0;
		gameObjects = 0;
	}
	
	/**
	 * Returns the current amount of frames rendered per second.
	 * @return Frames per second
	 */
	public int getFps() {
		return application.getTimer().getFPS();
	}
	
	/**
	 * Returns the current amount of updates per second.
	 * @return Updates per second
	 */
	public int getUps() {
		return application.getTimer().getUPS();
	}
	
	/**
	 * Returns the time elapsed since <code>glfwInit()</code> in seconds.
	 * @return System time in seconds
	 */
	public double getTime() {
		return application.getTimer().getTime();
	}
	
	/**
	 * @param rendered Amount of vertices rendered on the screen
	 * @param total Total amount of vertices
	 */
	public void setVertices(int rendered, int total) {
		renderedVertices = rendered;
		totalVertices = total;
	}
	
	public int getRenderedVertices() {
		return renderedVertices;
	}
	
	public int getTotalVertices() {
		return totalVertices;
	}
	
	/**
	 * Returns the version of the game, if the <code>version.txt</code> file is present,
	 * or an empty string.
	 */
	public String getVersion() {
		if (version != null) {
			return version;
		}
		
		String filePath = "version.txt";
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			version = reader.readLine();
		} catch (IOException e) {
			version = "";
		}
		
		return version;
	}
	
	public void addGameObject() {
		gameObjects++;
	}
	
	public void removeGameObject() {
		if (gameObjects > 0) {
			gameObjects--;
		}
	}
	
	public int getGameObjects() {
		return gameObjects;
	}
	
	public String getWindowSize() {
		return String.format("%sx%s", getWindowWidth(), getWindowHeight());
	}
	
	public int getWindowWidth() {
		return application.getWindow().getWidth();
	}
	
	public int getWindowHeight() {
		return application.getWindow().getHeight();
	}
}
