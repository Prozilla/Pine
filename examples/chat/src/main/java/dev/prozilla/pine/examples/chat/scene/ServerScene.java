package dev.prozilla.pine.examples.chat.scene;

import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.chat.server.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerScene extends Scene {
	
	private final Server server;
	
	public ServerScene(Server server) {
		this.server = server;
	}
	
	@Override
	protected void load() {
		super.load();
		
		Entity nodeRoot = world.addEntity(new NodeRootPrefab());
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		Entity layoutNode = nodeRoot.addChild(layoutPrefab);
		
		TextPrefab portPrefab = new TextPrefab("Port: " + server.getPort());
		layoutNode.addChild(portPrefab);
		
		String address = "Unknown";
		try {
			address = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		TextPrefab addressPrefab = new TextPrefab("IP address: " + address);
		layoutNode.addChild(addressPrefab);
	}
}
