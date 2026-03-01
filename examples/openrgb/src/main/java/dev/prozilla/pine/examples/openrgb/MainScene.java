package dev.prozilla.pine.examples.openrgb;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.ColorParser;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.entity.prefab.ui.*;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.init.InitSystem;

import java.io.IOException;

public class MainScene extends Scene {
	
	private OpenRGBClient client;
	private TextInputNode inputNode;
	private ColorParser colorParser;
	
	private static final String COLOR_INPUT_TAG = "color-input";
	
	public boolean connect() {
		if (client.isConnected())
			return true;
		
		try {
			client.connect();
			logger.log(String.format("Connected successfully to %s:%s", client.getHost(), client.getPort()));
		} catch (IOException e) {
			logger.error(String.format("Failed to connect to %s:%s", client.getHost(), client.getPort()), e);
			application.stop();
		}
		
		return client.isConnected();
	}
	
	public void disconnect() {
		if (!client.isConnected())
			return;
		
		try {
			client.disconnect();
			logger.log("Disconnected successfully");
		} catch (IOException e) {
			logger.error("Failed to disconnect", e);
		}
	}
	
	public int getDeviceCount() {
		if (!connect())
			return 0;
		
		try {
			return client.getControllerCount();
		} catch (IOException e) {
			logger.error("Failed to get device count", e);
			connect();
			return 0;
		}
	}
	
	public void setDevicesColor(Color color) {
		if (!connect())
			return;
		
		int deviceCount = getDeviceCount();
		
		for (int i = 0; i < deviceCount; i++) {
			try {
				client.setCustomMode(i);
				client.updateLEDs(i, color);
				logger.log("Updated device LEDs: device " + i);
			} catch (IOException e) {
				logger.error("Failed to update device " + i + ", reconnecting...", e);
				if (!connect())
					return;
			}
		}
	}
	
	public void setInputValue(String value) {
		inputNode.setText(value);
	}
	
	public void apply() {
		if (colorParser.parse(inputNode.getText())) {
			setDevicesColor(colorParser.getResult());
		}
	}
	
	@Override
	protected void load() {
		super.load();
		
		// Create system that will initialize the input node
		world.addSystem(new InitSystem(TextInputNode.class) {
			@Override
			public void initSystem(World world) {
				setRequiredTag(COLOR_INPUT_TAG);
				super.initSystem(world);
			}
			
			@Override
			protected void process(EntityChunk chunk) {
				inputNode = chunk.getComponent(TextInputNode.class);
			}
		});
		
		// Initialize fields
		client = new OpenRGBClient(application.getWindow().getTitle());
		colorParser = new ColorParser();
		
		NodeRootPrefab nodeRootPrefab = new NodeRootPrefab();
		
		LayoutPrefab menuPrefab = new LayoutPrefab();
		menuPrefab.setGap(new Dimension(16));
		menuPrefab.setAnchor(GridAlignment.CENTER);
		menuPrefab.setAlignment(EdgeAlignment.CENTER);
		menuPrefab.setDirection(Direction.DOWN);
		menuPrefab.setBackgroundColor(Color.white().setAlpha(0.65f));
		menuPrefab.setPadding(new DualDimension(16));
		
		TextPrefab titleTextPrefab = new TextPrefab(application.getWindow().getTitle());
		titleTextPrefab.setColor(Color.black());
		
		TextInputPrefab inputPrefab = new TextInputPrefab();
		inputPrefab.setColor(Color.black());
		inputPrefab.setBackgroundColor(Color.lightGray());
		inputPrefab.setPadding(new DualDimension(16, 8));
		inputPrefab.setTag(COLOR_INPUT_TAG);
		
		LayoutPrefab colorsPrefab = new LayoutPrefab();
		colorsPrefab.setDirection(Direction.RIGHT);
		colorsPrefab.setGap(new Dimension(8));
		
		TextButtonPrefab redButtonPrefab = new TextButtonPrefab("Red");
		redButtonPrefab.setColor(Color.black());
		redButtonPrefab.setBackgroundColor(Color.red());
		redButtonPrefab.setPadding(new DualDimension(16, 8));
		redButtonPrefab.setClickCallback((button) -> setInputValue("red"));
		
		TextButtonPrefab greenButtonPrefab = new TextButtonPrefab("Green");
		greenButtonPrefab.setColor(Color.black());
		greenButtonPrefab.setBackgroundColor(Color.green());
		greenButtonPrefab.setPadding(new DualDimension(16, 8));
		greenButtonPrefab.setClickCallback((button) -> setInputValue("green"));
		
		TextButtonPrefab blueButtonPrefab = new TextButtonPrefab("Blue");
		blueButtonPrefab.setColor(Color.black());
		blueButtonPrefab.setBackgroundColor(Color.blue());
		blueButtonPrefab.setPadding(new DualDimension(16, 8));
		blueButtonPrefab.setClickCallback((button) -> setInputValue("blue"));
		
		colorsPrefab.addChildren(redButtonPrefab, greenButtonPrefab, blueButtonPrefab);
		
		TextButtonPrefab applyButtonPrefab = new TextButtonPrefab("Apply");
		applyButtonPrefab.setColor(Color.black());
		applyButtonPrefab.setPadding(new DualDimension(16, 8));
		applyButtonPrefab.setClickCallback((button) -> apply());
		
		menuPrefab.addChildren(titleTextPrefab, inputPrefab, colorsPrefab, applyButtonPrefab);
		nodeRootPrefab.addChild(menuPrefab);
		world.addEntity(nodeRootPrefab);
		
		connect();
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		if (getInput().getKeyDown(Key.ENTER)) {
			apply();
		}
	}
	
	@Override
	public void destroy() throws IllegalStateException {
		super.destroy();
		disconnect();
	}
	
}