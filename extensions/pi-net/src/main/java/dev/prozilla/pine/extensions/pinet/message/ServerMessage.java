package dev.prozilla.pine.extensions.pinet.message;

import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.connection.Connection;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequest;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequestHandler;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponse;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponseHandler;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.session.ClientSession;
import dev.prozilla.pine.extensions.pinet.session.ServerSession;
import dev.prozilla.pine.extensions.pinet.session.Session;

/**
 * A message between the server and a client that may contain a {@link Packet} as a payload.
 *
 * <p>
 *     A message sent by a client to the server is a {@link ServerRequest} and handled by a {@link ServerRequestHandler}
 *     and a message sent by the server to a client is a {@link ServerResponse} and handled by a {@link ServerResponseHandler}.
 * </p>
 * <p>
 *     The usual flow of server messages is:
 *     <table>
 *         <tr>
 *             <td></td>
 *             <td>{@link ServerResponseHandler}</td>
 *             <td>{@link ClientSession}</td>
 *             <td>{@link Connection}</td>
 *             <td>...</td>
 *             <td>{@link Connection}</td>
 *             <td>{@link ServerSession}</td>
 *             <td>{@link Server}</td>
 *             <td>{@link ServerRequestHandler}</td>
 *         </tr>
 *         <tr>
 *             <td>Client &rightarrow; server</td>
 *             <td><i>{@link Packet}*</i></td>
 *             <td>{@link Packet}</td>
 *             <td>&rightarrow;</td>
 *             <td>...</td>
 *             <td>&rightarrow;</td>
 *             <td>{@link Packet}</td>
 *             <td>{@link Packet} &rightarrow; {@link ServerRequest}</td>
 *             <td>{@link ServerRequest}</td>
 *         </tr>
 *         <tr>
 *             <td>Server &rightarrow; client</td>
 *             <td>{@link ServerResponse}</td>
 *             <td>{@link ServerResponse} &leftarrow; {@link Packet}</td>
 *             <td>&leftarrow;</td>
 *             <td>...</td>
 *             <td>&leftarrow;</td>
 *             <td>&leftarrow;</td>
 *             <td>{@link Packet}</td>
 *             <td><i>{@link Packet}*</i></td>
 *         </tr>
 *     </table>
 *     <i>
 *         *Usually messages are sent by the {@link ClientSession} and {@link ServerRequestHandler},
 *         but it is also possible for messages to be sent by the {@link ServerResponseHandler}
 *         (e.g., a reply to a response from the server) and the {@link Server} (e.g., an automated message).
 *     </i>
 * </p>
 */
public abstract class ServerMessage {
	
	private Packet payload;
	/** The session corresponding to the sender of this message. */
	protected Session source;
	
	private boolean isAcknowledged;
	private boolean isDiscarded;
	
	public ServerMessage(Packet payload, Session source) {
		this.payload = payload;
		this.source = source;
		
		isAcknowledged = false;
		isDiscarded = false;
	}
	
	/**
	 * @return The payload attached to this message, or {@code null}.
	 */
	public Packet getPayload() {
		return payload;
	}
	
	public void setPayload(Packet payload) {
		this.payload = payload;
	}
	
	/**
	 * @return The session corresponding to the sender of this message.
	 */
	public Session getSource() {
		return source;
	}
	
	public void setSource(Session source) {
		this.source = source;
	}
	
	/**
	 * Sends the payload of this message back to its sender.
	 */
	public void echo() {
		if (payload != null) {
			reply(payload);
		}
	}
	
	/**
	 * Sends a packet as a reply to this message to its sender and marks this message as acknowledged.
	 * @param packet The packet to send
	 */
	public void reply(Packet packet) {
		acknowledge();
		if (source != null) {
			source.send(packet);
		}
	}
	
	/**
	 * Checks whether this message has been acknowledged.
	 *
	 * <p>
	 *     A message is marked as acknowledged when a reply is sent.
	 * </p>
	 * @return {@code true} if this message has been acknowledged.
	 */
	public boolean isAcknowledged() {
		return isAcknowledged;
	}
	
	/**
	 * Marks this message as acknowledged.
	 */
	public void acknowledge() {
		isAcknowledged = true;
	}
	
	/**
	 * Checks whether this message has been discarded.
	 * @return {@code true} if this message has been discarded.
	 */
	public boolean isDiscarded() {
		return isDiscarded;
	}
	
	/**
	 * Discards this message.
	 */
	public void discard() {
		isDiscarded = true;
	}
	
}
