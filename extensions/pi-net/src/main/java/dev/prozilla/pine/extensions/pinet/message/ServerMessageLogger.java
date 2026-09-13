package dev.prozilla.pine.extensions.pinet.message;

import dev.prozilla.pine.common.logging.Logger;

/**
 * An implementation of {@link ServerMessageAdapter} that logs all incoming messages.
 */
public class ServerMessageLogger implements ServerMessageAdapter {
	
	private Logger logger;
	
	public ServerMessageLogger() {
		this(Logger.system);
	}
	
	public ServerMessageLogger(Logger logger) {
		this.logger = logger;
	}
	
	/**
	 * Logs the given message.
	 * @param message The message to log
	 */
	@Override
	public void handleMessage(ServerMessage message) {
		getLogger().log(message);
	}
	
	public Logger getLogger() {
		return logger != null ? logger : Logger.system;
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
}
