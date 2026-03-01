package dev.prozilla.pine.examples.openrgb;

import dev.prozilla.pine.common.IntEnum;

public enum OpenRGBCommand implements IntEnum {
	REQUEST_CONTROLLER_COUNT(0),
	REQUEST_CONTROLLER_DATA(1),
	GET_PROTOCOL_VERSION(40),
	SET_CLIENT_NAME(50),
	REQUEST_PROFILE_LIST(150),
	SAVE_PROFILE(151),
	LOAD_PROFILE(152),
	DELETE_PROFILE(153),
	RESIZE_ZONE(1000),
	UPDATE_LEDS(1050),
	UPDATE_ZONE_LEDS(1051),
	UPDATE_SINGLE_LED(1052),
	SET_CUSTOM_MODE(1100),
	UPDATE_MODE(1101);
	
	private final int packetId;
	
	OpenRGBCommand(int packetId) {
		this.packetId = packetId;
	}
	
	@Override
	public int getValue() {
		return packetId;
	}
	
}
