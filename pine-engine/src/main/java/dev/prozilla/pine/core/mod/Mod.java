package dev.prozilla.pine.core.mod;

public interface Mod {
	
	void init();
	
	void input(float deltaTime);
	
	void update(float deltaTime);
	
	void render();
	
	void destroy();

}
