package dev.prozilla.pine.examples.chat.scene.server;

import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.examples.chat.entity.ChatPrefab;
import dev.prozilla.pine.examples.chat.net.server.Server;
import dev.prozilla.pine.examples.chat.scene.SceneBase;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerScene extends SceneBase {
	
	private final Server server;
	
	public ServerScene(Server server) {
		this.server = server;
	}
	
	@Override
	protected void load() {
		super.load();
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		Entity layout = nodeRoot.addChild(layoutPrefab);
		
		layout.addChild(new ChatPrefab(server.getHost(), font));
		
		TextPrefab portPrefab = new TextPrefab("Port: " + server.getPort());
		portPrefab.setFont(font);
		layout.addChild(portPrefab);
		
		String address = "Unknown";
		try {
			address = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		TextPrefab addressPrefab = new TextPrefab("IP address: " + address);
		addressPrefab.setFont(font);
		layout.addChild(addressPrefab);
	}
}
