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
import io.gitlab.mguimard.openrgb.client.OpenRGBClient;
import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;
import io.gitlab.mguimard.openrgb.entity.OpenRGBDevice;

import java.io.IOException;
import java.util.Arrays;

public class MainScene extends Scene {
	
	public OpenRGBClient client;
	
	public static final String OPENRGB_SERVER_HOST = "localhost";
	public static final int OPENRGB_SERVER_PORT = 6742;
	
	public boolean connect() {
		if (client != null && !disconnect())
			return false;
		
		String host = OPENRGB_SERVER_HOST;
		int port = OPENRGB_SERVER_PORT;
		client = new OpenRGBClient(host, port, application.getWindow().getTitle());
		
		try {
			client.connect();
			logger.log(String.format("Connected successfully to %s:%s", host, port));
			return true;
		} catch (IOException e) {
			logger.error(String.format("Failed to connect to %s:%s", host, port),e);
			client = null;
			application.stop();
			return false;
		}
	}
	
	public boolean disconnect() {
		if (client == null)
			return false;
		
		try {
			client.disconnect();
			client = null;
			return true;
		} catch (IOException e) {
			logger.error("Failed to disconnect",e);
			return false;
		}
	}
	
	public OpenRGBDevice[] getDevices() {
		if (client == null && !connect())
			return new OpenRGBDevice[0];
		
		OpenRGBDevice[] devices;
		try {
			devices = new OpenRGBDevice[client.getControllerCount()];
			for (int i = 0; i < devices.length; i++) {
				devices[i] = client.getDeviceController(i);
			}
		} catch (IOException e) {
			logger.error("Failed to get devices", e);
			return new OpenRGBDevice[0];
		}
		return devices;
	}
	
	public void setDevicesColor(Color color) {
		if (client == null && !connect())
			return;
		
		OpenRGBDevice[] devices = getDevices();
		OpenRGBColor value = OpenRGBUtils.convertColor(color);
		for (int i = 0; i < devices.length; i++) {
			OpenRGBDevice device = devices[i];
			OpenRGBColor[] colors = new OpenRGBColor[device.getColors().size()];
			Arrays.fill(colors, value);
			try {
				client.updateLeds(i, colors);
				logger.log("Updated device LEDs of " + device.getName());
			} catch (IOException e) {
				logger.error("Failed to set device color of " + device.getName(), e);
				if (!connect())
					return;
			}
		}
	}
	
	@Override
	protected void load() {
		super.load();
		
		// Create prefabs
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
		
		// Instantiate prefabs
		world.addEntity(nodeRootPrefab);
	}
	
	@Override
	public void destroy() throws IllegalStateException {
		super.destroy();
		disconnect();
	}
}
