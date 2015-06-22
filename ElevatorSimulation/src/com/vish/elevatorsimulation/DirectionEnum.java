package com.vish.elevatorsimulation;

/**
 * 
 * @author Vish
 * 
 * Enum class to indicate direction/state of elevator
 *
 */
public enum DirectionEnum {
	UP ("Up"),
	DOWN ("Down"),
	MAINTENANCE ("Maintenance"),
	IDLE ("Idle");

	private String direction;

	private DirectionEnum (String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}
}
