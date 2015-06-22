package com.vish.elevatorsimulation;

/**
 * Elevator Controller interface
 */
import java.util.List;

public interface ElevatorController {
	/**
	 * Get elevator list
	 */
	public List<Elevator> getElevators();
	
	/**
	 * 
	 * @param floor 
	 * @param direction
	 * @param passengerId
	 * @return
	 */
	public Elevator callElevator(int floor, DirectionEnum direction, int passengerId);
}
