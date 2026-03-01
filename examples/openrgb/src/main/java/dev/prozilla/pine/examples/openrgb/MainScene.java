package dev.prozilla.pine.examples.openrgb;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.scene.Scene;

import java.io.IOException;

public class MainScene extends Scene {
	
	private OpenRGBClient client;
	
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
				if (!connect()) return;
			}
		}
	}
	
	@Override
	protected void load() {
		super.load();
		
		client = new OpenRGBClient(application.getWindow().getTitle());
		
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
		
		TextButtonPrefab redButtonPrefab = new TextButtonPrefab("Red");
		redButtonPrefab.setColor(Color.black());
		redButtonPrefab.setPadding(new DualDimension(16, 8));
		redButtonPrefab.setClickCallback((button) -> setDevicesColor(Color.red()));
		
		TextButtonPrefab greenButtonPrefab = new TextButtonPrefab("Green");
		greenButtonPrefab.setColor(Color.black());
		greenButtonPrefab.setPadding(new DualDimension(16, 8));
		greenButtonPrefab.setClickCallback((button) -> setDevicesColor(Color.green()));
		
		TextButtonPrefab blueButtonPrefab = new TextButtonPrefab("Blue");
		blueButtonPrefab.setColor(Color.black());
		blueButtonPrefab.setPadding(new DualDimension(16, 8));
		blueButtonPrefab.setClickCallback((button) -> setDevicesColor(Color.blue()));
		
		menuPrefab.addChildren(titleTextPrefab, redButtonPrefab, greenButtonPrefab, blueButtonPrefab);
		nodeRootPrefab.addChild(menuPrefab);
		world.addEntity(nodeRootPrefab);
		
		connect();
	}
	
	@Override
	public void destroy() throws IllegalStateException {
		super.destroy();
		disconnect();
	}
}