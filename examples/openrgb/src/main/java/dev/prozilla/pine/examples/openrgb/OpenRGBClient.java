package dev.prozilla.pine.examples.openrgb;

import dev.prozilla.pine.common.system.Color;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * Socket client for the OpenRGB SDK server.
 */
public class OpenRGBClient {
    
    private final String host;
    private final int port;
    private final String clientName;
    
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private int negotiatedProtocolVersion = 0;
    
    private static final int NET_PACKET_ID_REQUEST_CONTROLLER_COUNT = 0;
    private static final int NET_PACKET_ID_REQUEST_CONTROLLER_DATA = 1;
    private static final int NET_PACKET_ID_REQUEST_PROTOCOL_VERSION = 40;
    private static final int NET_PACKET_ID_SET_CLIENT_NAME = 50;
    private static final int NET_PACKET_ID_RGBCONTROLLER_SETCUSTOMMODE = 1100;
    private static final int NET_PACKET_ID_RGBCONTROLLER_UPDATELEDS = 1050;
    
    private static final byte[] MAGIC = {'O', 'R', 'G', 'B'};
    private static final int CLIENT_PROTOCOL_VERSION = 5;
    private static final int HEADER_SIZE = 16;
    
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 6742;
    
    public OpenRGBClient(String clientName) {
        this(clientName, DEFAULT_HOST, DEFAULT_PORT);
    }
    
    public OpenRGBClient(String clientName, String host, int port) {
        this.clientName = clientName;
        this.host = host;
        this.port = port;
    }
    
    public String getHost() {
        return host;
    }
    
    public int getPort() {
        return port;
    }
    
    /**
     * Creates a new socket connection and closes any existing connection.
     */
    public void connect() throws IOException {
        if (socket != null) {
            disconnect();
        }
        socket = new Socket(host, port);
        in = new BufferedInputStream(socket.getInputStream());
        out = new BufferedOutputStream(socket.getOutputStream());
        negotiatedProtocolVersion = negotiateProtocol();
        setClientName(clientName);
    }
    
    /**
     * Closes the active socket connection, if there is one.
     */
    public void disconnect() throws IOException {
        if (socket != null) {
            socket.close();
            socket = null;
        }
        in = null;
        out = null;
    }
    
    /**
     * Checks if the client is connected.
     */
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
    
    private int negotiateProtocol() throws IOException {
        ByteBuffer data = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        data.putInt(CLIENT_PROTOCOL_VERSION);
        sendPacket(0, NET_PACKET_ID_REQUEST_PROTOCOL_VERSION, data.array());
        byte[] response = readPacketData();
        int serverVersion = ByteBuffer.wrap(response).order(ByteOrder.LITTLE_ENDIAN).getInt();
        return Math.min(CLIENT_PROTOCOL_VERSION, serverVersion);
    }
    
    public int getProtocolVersion() {
        return negotiatedProtocolVersion;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String name) throws IOException {
        byte[] nameBytes = (name + "\0").getBytes(StandardCharsets.US_ASCII);
        sendPacket(0, NET_PACKET_ID_SET_CLIENT_NAME, nameBytes);
    }
    
    public int getControllerCount() throws IOException {
        sendPacket(0, NET_PACKET_ID_REQUEST_CONTROLLER_COUNT, new byte[0]);
        byte[] response = readPacketData();
        return ByteBuffer.wrap(response).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
    
    public int getLEDCount(int deviceIndex) throws IOException {
        byte[] requestData = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
                                 .putInt(negotiatedProtocolVersion).array();
        sendPacket(deviceIndex, NET_PACKET_ID_REQUEST_CONTROLLER_DATA, requestData);
        byte[] response = readPacketData();
        return parseLEDCount(response);
    }
    
    /**
     * Parses the LED count from a controller data response packet.
     */
    private int parseLEDCount(byte[] data) {
        // TO DO: Put controller data into separate class
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        
        buffer.getInt(); // data_size
        buffer.getInt(); // device_type
        
        skipString(buffer); // name
        skipString(buffer); // vendor
        skipString(buffer); // description
        skipString(buffer); // version
        skipString(buffer); // serial
        skipString(buffer); // location
        
        int numModes = buffer.getShort() & 0xFFFF;
        buffer.getInt(); // active_mode
        
        for (int i = 0; i < numModes; i++) {
            skipString(buffer);
            buffer.getInt(); // value
            buffer.getInt(); // flags
            buffer.getInt(); // speed_min
            buffer.getInt(); // speed_max
            if (negotiatedProtocolVersion >= 3) {
                buffer.getInt(); // brightness_min
                buffer.getInt(); // brightness_max
            }
            buffer.getInt(); // colors_min
            buffer.getInt(); // colors_max
            buffer.getInt(); // speed
            if (negotiatedProtocolVersion >= 3) {
                buffer.getInt(); // brightness
            }
            buffer.getInt(); // direction
            buffer.getInt(); // color_mode
            
            int numModeColors = buffer.getShort() & 0xFFFF;
            buffer.position(buffer.position() + numModeColors * 4);
        }
        
        int numZones = buffer.getShort() & 0xFFFF;
        
        for (int i = 0; i < numZones; i++) {
            skipString(buffer); // zone_name
            buffer.getInt(); // zone_type
            buffer.getInt(); // leds_min
            buffer.getInt(); // leds_max
            buffer.getInt(); // num_leds
            int matSize = buffer.getInt();
            if (matSize > 0) {
                buffer.getInt(); // matrix height
                buffer.getInt(); // matrix width
                buffer.position(buffer.position() + matSize * 4);
            }
            if (negotiatedProtocolVersion >= 4) {
                buffer.mark();
	            for (int j = 0; j < 4; j++) {
		            buffer.get();
	            }
	            buffer.reset();
                int numSegments = buffer.getInt();
                for (int s = 0; s < numSegments; s++) {
                    skipString(buffer); // segment_name
                    buffer.getInt(); // segment_type
                    buffer.getInt(); // start_idx
                    buffer.getInt(); // leds_count
                }
            }
        }
        
        return buffer.getShort() & 0xFFFF; // num_leds
    }
    
    private void skipString(ByteBuffer buf) {
        int len = buf.getShort() & 0xFFFF;
        buf.position(buf.position() + len);
    }
    
    public void setCustomMode(int deviceIndex) throws IOException {
        sendPacket(deviceIndex, NET_PACKET_ID_RGBCONTROLLER_SETCUSTOMMODE, new byte[0]);
    }
    
    public void updateLEDs(int deviceIndex, Color color) throws IOException {
        int ledCount = getLEDCount(deviceIndex);
        updateLEDs(deviceIndex, color, ledCount);
    }
    
    public void updateLEDs(int deviceIndex, Color color, int ledCount) throws IOException {
        int dataSize = 4 + 2 + ledCount * 4;
        ByteBuffer buf = ByteBuffer.allocate(dataSize).order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(dataSize);
        buf.putShort((short) ledCount);
        for (int i = 0; i < ledCount; i++) {
            buf.put((byte)Math.round(color.getRed() * 255f));
            buf.put((byte)Math.round(color.getGreen() * 255f));
            buf.put((byte)Math.round(color.getBlue() * 255f));
            buf.put((byte)0x00);
        }
        sendPacket(deviceIndex, NET_PACKET_ID_RGBCONTROLLER_UPDATELEDS, buf.array());
    }
    
    private void sendPacket(int deviceIndex, int packetId, byte[] data) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(HEADER_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        header.put(MAGIC);
        header.putInt(deviceIndex);
        header.putInt(packetId);
        header.putInt(data.length);
        out.write(header.array());
        out.write(data);
        out.flush();
    }
    
    private byte[] readPacketData() throws IOException {
        byte[] header = in.readNBytes(HEADER_SIZE);
        if (header.length < HEADER_SIZE)
            throw new IOException("Connection closed while reading header");
        int dataSize = ByteBuffer.wrap(header, 12, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        if (dataSize == 0)
            return new byte[0];
        byte[] data = in.readNBytes(dataSize);
        if (data.length < dataSize)
            throw new IOException("Connection closed while reading packet data");
        return data;
    }
    
}