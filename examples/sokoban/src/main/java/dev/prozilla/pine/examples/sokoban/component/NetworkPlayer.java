package dev.prozilla.pine.examples.sokoban.component;

import dev.prozilla.pine.core.component.Component;

public class NetworkPlayer extends Component {
	
	public final int id;
	public boolean awaitingConfirm;
	
	public NetworkPlayer(int id) {
		this.id = id;
	}
	
}
